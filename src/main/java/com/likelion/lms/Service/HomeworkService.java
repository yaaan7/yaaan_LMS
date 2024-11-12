package com.likelion.lms.Service;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.User;
import com.likelion.lms.Domain.UserHomework;
import com.likelion.lms.Repository.HomeworkRepository;
import com.likelion.lms.Repository.UserHomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final UserHomeworkRepository userHomeworkRepository;

    // 생성자 주입을 통해 repository 주입
    @Autowired
    public HomeworkService(HomeworkRepository homeworkRepository, UserHomeworkRepository userHomeworkRepository) {
        this.homeworkRepository = homeworkRepository;
        this.userHomeworkRepository = userHomeworkRepository;
    }

    // 전체 조회
    public List<Homework> getAllHomework() {
        return homeworkRepository.findAll();
    }

    // 페이징 조회
    public Page<Homework> getHomeworkByPageAndSort(int page, int size, String criterion) {
        Sort sort = Sort.by(Sort.Direction.ASC, criterion.equals("createdDate") ? "createdDate" : "dueDate");
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return homeworkRepository.findAll(pageRequest);
    }

    // 세부 조회
    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다. ID: " + id));
    }

    // 관리자 세부조회
    public List<UserHomework> getSubmittedFilesByHomeworkId(Long homeworkId) {
        return userHomeworkRepository.findByHomeworkId(homeworkId);
    }

    // 저장
    public void saveHomework(String cate, String title, String description, LocalDate dueDate, LocalTime dueTime, MultipartFile file) {
        Homework homework = new Homework();
        homework.setCate(cate);
        homework.setTitle(title);
        homework.setDescription(description);
        homework.setDueDate(dueDate);
        homework.setDueTime(String.valueOf(dueTime));

        // 과제를 저장/수정하기 위해 HomeworkRepository에 저장
        homeworkRepository.save(homework);
    }

    public String saveFile(MultipartFile file) {
        String uploadDir = "uploads/";
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        try {
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return uploadDir + fileName;
    }

    // 수정
    public Homework updateHomework(Long id, Homework updatedHomework) {
        return homeworkRepository.findById(id).map(homework -> {
            homework.setTitle(updatedHomework.getTitle());
            homework.setDescription(updatedHomework.getDescription());
            homework.setDueDate(updatedHomework.getDueDate());
            homework.setDueTime(updatedHomework.getDueTime());
            return homeworkRepository.save(homework);
        }).orElseThrow(() -> new IllegalArgumentException("해당 과제가 존재하지 않습니다. ID: " + id));
    }

    // 삭제
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id);
    }

    // 과제 조회
    public List<UserHomework> getSubmittedFilesByHomeworkIdAndUserId(Long homeworkId, Long userId) {
        return userHomeworkRepository.findByHomeworkIdAndUserId(homeworkId, userId);
    }

    public Map<User, List<UserHomework>> getSubmittedFilesGroupedByUser(Long homeworkId) {
        List<UserHomework> submittedFiles = userHomeworkRepository.findByHomeworkId(homeworkId);
        return submittedFiles.stream()
                .collect(Collectors.groupingBy(UserHomework::getUser));
    }

    // 과제 저장 메서드
    public Homework saveHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }
}



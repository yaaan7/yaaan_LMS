package com.likelion.lms.Service;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.User;
import com.likelion.lms.Domain.UserHomework;
import com.likelion.lms.Repository.HomeworkRepository;
import com.likelion.lms.Repository.UserHomeworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeworkService {

    @Autowired
    private final HomeworkRepository homeworkRepository;
    @Autowired
    private UserHomeworkRepository userHomeworkRepository;
    public HomeworkService(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    // 전체 조회
    public List<Homework> getAllHomework()  {
        return homeworkRepository.findAll();
    }

    //페이징 조회
    public Page<Homework> getHomeworkByPageAndSort(int page, int size, String criterion) {
        // 과제 목록을 조회하는 메서드
        Sort sort = Sort.by(Sort.Direction.ASC, criterion.equals("createdDate") ? "createdDate" : "dueDate");
        //정렬 방향을 지정
        //조건 : criterion == createdDate, 참이면 createdDate, 거짓이면 dueDate를 기준으로 정렬
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return homeworkRepository.findAll(pageRequest);
    }

    // 세부 조회
    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 과제를 찾을 수 없습니다. ID: " + id));
        //orElseThrow를 이용해 해당 과제가 없다면 예외, 있다면 return하게 만듦
    }
    // 관리자 세부조회
    public List<UserHomework> getSubmittedFilesByHomeworkId(Long homeworkId) {
        //Long homeworkId를 입력받아
        return userHomeworkRepository.findByHomeworkId(homeworkId);
        //해당 과제에 제출한 파일을 반환하는 메서드
    }

    // 저장
    public Homework saveHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }
    /*save(): id값이 존재하지 않는다면 insert()기능을 하여 데이터를 저장하고
    id값이 존재하면 데이터를 덮어씌워 수정하는 메서드*/

    // 수정
    public Homework updateHomework(Long id, Homework updatedHomework) {
        return homeworkRepository.findById(id).map(homework -> {
            //findById() : ID 값으로 엔티티를 가져오는 메서드
            homework.setTitle(updatedHomework.getTitle());
            homework.setDescription(updatedHomework.getDescription());
            homework.setDueDate(updatedHomework.getDueDate());
            homework.setDueTime(updatedHomework.getDueTime());
            //homework 모델에? 제목, 상세 설명, 마감일, 마감 시간 업데이트
            return homeworkRepository.save(homework);
        }).orElseThrow(() -> new IllegalArgumentException("해당 과제가 존재하지 않습니다. ID: " + id)); //예외 처리!
    }

    // 삭제
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id);
    }
    /*deleteById() : Id를 받아 그에 해당하는 엔티티를 삭제하는 메서드
    내부적으로 id에 대한 null체크를 해준다!*/

    //과제 조회
    public List<UserHomework> getSubmittedFilesByHomeworkIdAndUserId(Long homeworkId, Long userId) {
        return userHomeworkRepository.findByHomeworkIdAndUserId(homeworkId, userId);
        //사용자Id와 과제Id를 받아 해당 과제 목록을 찾아 반환
    }

    public Map<User, List<UserHomework>> getSubmittedFilesGroupedByUser(Long homeworkId) {
        //과제 Id를 받아 User를 기준으로 과제 목록을 그룹화하여 반환하는 메서드
        List<UserHomework> submittedFiles = userHomeworkRepository.findByHomeworkId(homeworkId);


        return submittedFiles.stream()
                .collect(Collectors.groupingBy(UserHomework::getUser));
    }

}
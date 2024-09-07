package com.likelion.lms.Service;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Repository.HomeworkRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;

    public HomeworkService(HomeworkRepository homeworkRepository) {
        this.homeworkRepository = homeworkRepository;
    }

    // 전체 조회
    public List<Homework> getAllHomework()  {
        return homeworkRepository.findAll();
    }

    //페이징 조회
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

    // 저장
    public Homework saveHomework(Homework homework) {
        return homeworkRepository.save(homework);
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
}
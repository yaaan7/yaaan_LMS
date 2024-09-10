package com.likelion.lms.Controller;

import com.likelion.lms.Domain.Homework;
import com.likelion.lms.Domain.User;
import com.likelion.lms.Domain.UserHomework;
import com.likelion.lms.Service.HomeworkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    // 목록 조회
    @GetMapping("/list/{page}")
    public String homework_list(
            @PathVariable int page,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            Model model, HttpSession session) {
        int size = 5;
        int pageIndex = page - 1;
        Page<Homework> homeworkPage = homeworkService.getHomeworkByPageAndSort(pageIndex, size, sortBy);
        List<Homework> homeworkList = homeworkPage.getContent();
        model.addAttribute("homeworkList", homeworkList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", homeworkPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        model.addAttribute("isAdmin", isAdmin);

        return "homework/homework_list";
    }

    // 세부 조회
// 세부 조회
    @GetMapping("/homework/{id}")
    public String homework_detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        Homework homework = homeworkService.getHomeworkById(id);
        if () {


            return "homework/homework_admin";
        } else {

            return "homework/homework";
        }
    }

    // 새글 쓰기
    @GetMapping

    // 업로드
    @PostMapping

    // 수정하기
    @GetMapping("/homework/edit/{id}")
    public String homework_edit(@PathVariable Long id, Model model) {
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null) {

        }
        model.addAttribute("homework", homework);

    }
    // 수정 저장
    @PutMapping

    }
}

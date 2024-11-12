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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @PostMapping("/save")
    public String saveHomework(@ModelAttribute Homework homework, RedirectAttributes redirectAttributes) {
        // 과제 저장
        homeworkService.saveHomework(homework);

        // 과제 목록 화면으로 리디렉션
        redirectAttributes.addFlashAttribute("message", "과제가 부여되었습니다.");
        return "redirect:/homework/homework_list";  // homeworklist.html로 리디렉션
    }

    @GetMapping("/list")
    public String getHomeworkList(Model model) {
        // 과제 목록 가져오기
        List<Homework> homeworkList = homeworkService.getAllHomework();
        model.addAttribute("homeworkList", homeworkList);
        return "redirect:/homework/homework_list";  // homeworklist.html로 반환
    }



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
    @GetMapping("/homework/{id}")
    public String homework_detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        Homework homework = homeworkService.getHomeworkById(id);

        if (isAdmin != null && isAdmin) {
            Map<User, List<UserHomework>> submittedFilesGroupedByUser = homeworkService.getSubmittedFilesGroupedByUser(id);
            model.addAttribute("homework", homework);
            model.addAttribute("submittedFilesGroupedByUser", submittedFilesGroupedByUser);
            return "homework/homework_admin";
        } else {
            Long userId = (Long) session.getAttribute("id");
            List<UserHomework> userSubmittedFiles = homeworkService.getSubmittedFilesByHomeworkIdAndUserId(id, userId);
            model.addAttribute("homework", homework);
            model.addAttribute("submittedFiles", userSubmittedFiles);
            return "homework/homework";
        }
    }

    // 새 과제 쓰기 페이지
    @GetMapping("/homework/new")
    public String homework_new() {
        return "/homework/write";
    }

    // 과제 업로드 (파일 포함)
    @PostMapping("/homework/post")
    public ResponseEntity<Homework> createHomework(@RequestParam String cate,
                                                   @RequestParam String title,
                                                   @RequestParam String description,
                                                   @RequestParam LocalDate dueDate,
                                                   @RequestParam LocalTime dueTime,
                                                   @RequestParam(required = false) MultipartFile file) {
        Homework homework = new Homework();
        homework.setCate(cate);
        homework.setTitle(title);
        homework.setDescription(description);
        homework.setDueDate(dueDate);
        homework.setDueTime(String.valueOf(dueTime));
        homework.setCreatedDate(LocalDateTime.now());

        // 파일 업로드 처리
        if (file != null && !file.isEmpty()) {
            String filePath = homeworkService.saveFile(file);  // 파일 저장 로직 (서비스 내 메서드 호출)
            homework.setFilePath(filePath);  // 저장된 파일 경로 설정
        }

        Homework createdHomework = homeworkService.saveHomework(homework);  // 과제 저장
        return ResponseEntity.ok(createdHomework);  // 저장된 과제 반환
    }

    // 과제 수정 페이지
    @GetMapping("/homework/edit/{id}")
    public String homework_edit(@PathVariable Long id, Model model) {
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null) {
            throw new IllegalArgumentException("해당 ID의 과제가 존재하지 않습니다: " + id);
        }
        model.addAttribute("homework", homework);
        return "homework/write";
    }

    // 과제 수정 저장
    @PutMapping("/homework/post/{id}")
    public ResponseEntity<Homework> updateHomework(@PathVariable Long id, @RequestBody Homework homework) {
        Homework updatedHomework = homeworkService.updateHomework(id, homework);
        return ResponseEntity.ok(updatedHomework);
    }

    // 과제 제출 (파일 제출)
    @PostMapping("/homework/save")
    public String submitHomework(@RequestParam String cate,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam LocalDate dueDate,
                                 @RequestParam LocalTime dueTime,
                                 @RequestParam(required = false) MultipartFile file) {
        // 과제 저장/수정 서비스 호출
        homeworkService.saveHomework(cate, title, description, dueDate, dueTime, file);

        // 저장 후 과제 목록 페이지로 리디렉션
        return "redirect:/homework/list";
    }
}

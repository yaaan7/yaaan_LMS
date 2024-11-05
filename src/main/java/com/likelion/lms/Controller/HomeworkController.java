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
//해당 클래스를 스프링 빈으로 사용할 수 있게 함.
//url로 Controller 클래스를 호출하려면 클래스가 스프링 빈으로 생성되어야함.
public class HomeworkController {

    @Autowired
    //필요한 의존 객체의 타입에 해당하는 빈을 찾아 주입함.
    private HomeworkService homeworkService;
    //이때 HomeworkService가 빈으로 등록되어야함.

    // 목록 조회
    @GetMapping("/list/{page}")
    //어떤 url로 접근할 것인지 정해주는 어노테이션
    public String homework_list(
            @PathVariable int page,
            //url를 통해 전달된 값을 파라미터로 받아오는 역할(page에 대입?)
            @RequestParam(defaultValue = "createdDate") String sortBy,
            // 값이 없을 때 createDate를 기본값으로 사용
            // 요청 매개변수(sortBy) 추출
            Model model, HttpSession session) {
        int size = 5; // 한 페이지에 보여줄 항목의 개수 -> 과제 개수
        int pageIndex = page - 1;
        Page<Homework> homeworkPage = homeworkService.getHomeworkByPageAndSort(pageIndex, size, sortBy);
        List<Homework> homeworkList = homeworkPage.getContent();
        //해당 페이지에 있는 목록을 getContent()메서드로 추출 -> 리스트에 저장..
        model.addAttribute("homeworkList", homeworkList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", homeworkPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        //Model에 목록, 현재 페이지, 총페이지, 정렬 기준, 관리자 여부 추가함
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        //관리자 권한을 가지고 있는지 없는지에 대한 값을 isAdmin에 저장
        model.addAttribute("isAdmin", isAdmin);
        return "homework/homework_list";
    }

    // 세부 조회
// 세부 조회
    @GetMapping("/homework/{id}")
    //서버의 리소스를 조회할 때 사용 ({id}:id는 변하는 값)
    public String homework_detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        Homework homework = homeworkService.getHomeworkById(id);

        if (isAdmin != null && isAdmin) {
            //관리자라면
            Map<User,List<UserHomework>> submittedFilesGroupedByUser = homeworkService.getSubmittedFilesGroupedByUser(id);
            model.addAttribute("homework",homework);
            //가져온 데이터를 model을 통해 view로 전달?
            model.addAttribute("submittedFilesGroupedByUser",submittedFilesGroupedByUser);


            return "homework/homework_admin";
            //보여줄 페이지 설정(homework라는 Model 사용 가능?) - homework_admin
        } else {
            //관리자가 아니라면
            Long userId = (Long) session.getAttribute("id");
            //세션에서 id를 가져옴
            List<UserHomework> userSubmittedFiles =homeworkService.getSubmittedFilesByHomeworkIdAndUserId(id,userId);
            //id가 제출한 파일 목록 조회
            System.out.println(userSubmittedFiles);
            model.addAttribute("homework",homework);
            model.addAttribute("submittedFiles",userSubmittedFiles);
            return "homework/homework";
        }
    }

    //새글 쓰기
    @GetMapping("/homework/new")
    public String homework_new() {
        return "/homework/write";
        //write 페이지로 이동
    }

    //과제 업로드
    @PostMapping("/homework/post")
    public ResponseEntity<Homework> createHomework(@RequestBody Homework homework) {

        homework.setCreatedDate(LocalDateTime.now());
        //생성 시간을 현재 시스템 시간으로 설정?
        Homework createdHomework = homeworkService.saveHomework(homework);
        //메서드를 호출하여 homework를 저장
        return ResponseEntity.ok(createdHomework);
    }

    //과제 수정하기
    @GetMapping("/homework/edit/{id}")
    public String homework_edit(@PathVariable Long id, Model model) {
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null) {
            throw new IllegalArgumentException("해당 ID의 과제가 존재하지 않습니다: "+id);
        }
        model.addAttribute("homework", homework);
        return "homework/write";


    }
    // 수정 저장
    @PutMapping("/homework/post/{id}")
    //데이터를 업데이트할 때 사용하는 매핑
    public ResponseEntity<Homework> updateHomework(@PathVariable Long id, @RequestBody Homework homework ){
        Homework updatedHomework=homeworkService.updateHomework(id,homework);
        return ResponseEntity.ok(updatedHomework);
    }
}

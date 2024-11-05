package com.likelion.lms.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    // 로그인 페이지 요청 처리
    @GetMapping("/login")
    public String loginPage() {
        return "home/home"; // home.html 파일을 반환
    }

    @PostMapping("/login")
    public String login(HttpSession session,
                        @RequestParam String username,
                        @RequestParam String password) {
        logger.info("로그인 시도 - username: {}, password: {}", username, password);

        // 관리자 로그인 처리
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("id", 1L);
            session.setAttribute("is_admin", true);
            logger.info("Admin 로그인 성공, admin_home으로 이동합니다.");
            return "redirect:/admin_home"; // 리다이렉트 추가

            // 일반 유저 로그인 처리
        } else if ("user".equals(username) && "user".equals(password)) {
            session.setAttribute("id", 2L);
            session.setAttribute("is_admin", false);
            logger.info("일반 유저 로그인 성공, user_home으로 이동합니다.");
            return "redirect:/user_home"; // 리다이렉트 추가

        } else {
            logger.info("로그인 실패: 잘못된 사용자 이름 또는 비밀번호");
            return "redirect:/login?error"; // 로그인 페이지로 리다이렉션, 오류 쿼리 파라미터 포함
        }
    }


}




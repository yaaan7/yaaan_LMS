package com.likelion.lms.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String home(HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
        if (isAdmin != null && isAdmin) {
            // admin_home으로 리디렉션
            return "redirect:/admin_home"; // URL이 변경됨
        } else if (isAdmin != null && !isAdmin) {
            // 일반 사용자일 경우 user_home으로 리디렉션
            return "redirect:/user_home"; // URL이 변경됨
        }
        // 로그인하지 않았을 경우 기본 홈 페이지로 리디렉션
        return "redirect:/login"; // 로그인 페이지로 이동
    }

    @GetMapping("/admin_home")
    public String adminHome() {
        return "home/admin_home"; // admin_home.html을 반환
    }

    @GetMapping("/user_home")
    public String userHome() {
        return "home/user_home"; // user_home.html을 반환
    }

}




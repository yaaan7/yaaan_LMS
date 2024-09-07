package com.likelion.lms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() { return "user/login";}

    @GetMapping("/about")
    public String about() { return "home/aboutme";}

    @GetMapping("/main")
    public String credit() { return "home/12th_credit";}

    @GetMapping("/list")
    public String homework_list() { return "homework/homework";}

    @GetMapping("/homework/{id}")
    public String homework_detail() { return "homework/homework_list";}

    @GetMapping("/error")
    public String error_page() { return "home/404";}

}
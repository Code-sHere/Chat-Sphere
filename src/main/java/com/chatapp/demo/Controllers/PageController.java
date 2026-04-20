package com.chatapp.demo.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "register";
    }

    @GetMapping("/chat")
    public String showChatPage() {
        return "chat";
    }

}
package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Passwordconfig;
import com.chatapp.demo.Service.Userservice;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private Userservice userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody passwordConfig.LoginRequest request,
            HttpSession session) {

        UserEntity user = userService.login(
                request.getEmail(),
                request.getPassword());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        // Create session
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());

        // Session valid for 2 days
        session.setMaxInactiveInterval(2 * 24 * 60 * 60);

        return ResponseEntity.ok("Login successful");
    }
}

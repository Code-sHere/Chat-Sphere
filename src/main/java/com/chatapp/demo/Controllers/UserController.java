package com.chatapp.demo.Controllers;

import java.io.File;
import java.io.IOException;

import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Service.Userservice;
import com.chatapp.demo.config.Passwordconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    @Autowired
    private Passwordconfig passwordConfig;

    @Autowired
    private Userservice userService;

    @PostMapping("/register")
    public String registerUser(
            UserEntity user)
            throws IOException {
        // Hash password
        String hashedPassword = passwordConfig.passwordEncoder().encode(
                user.getPassword());

        user.setPassword(hashedPassword);

        // Save to database
        userService.saveUser(user);

        return "redirect:/register";
    }

    @ResponseBody
    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {

        return userService.getAllUsers();

    }
}
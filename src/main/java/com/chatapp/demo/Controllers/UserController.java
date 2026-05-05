package com.chatapp.demo.Controllers;

import com.chatapp.demo.Service.MessageService;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Service.Userservice;
import com.chatapp.demo.Service.MessageService;
import com.chatapp.demo.Repository.Userrepository;
import com.chatapp.demo.config.Passwordconfig;
import org.springframework.security.core.Authentication;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    private final MessageService messageService;

    @Autowired
    private Passwordconfig passwordConfig;

    @Autowired
    private Userservice userService;

    @Autowired
    private Userrepository userrepository;

    UserController(MessageService messageService) {
        this.messageService = messageService;
    }

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

    @ResponseBody
    @GetMapping("/current-user")
    public Map<String, String> getCurrentUser(Authentication auth) {

        String email = auth.getName();
        UserEntity user = userrepository.findByEmail(email);
        return Map.of(
                "username", user.getName(),
                "email", user.getEmail()
        );
    }
    
}
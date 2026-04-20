package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/private")
    public ChatEntity createPrivateChat(
            @RequestParam Long userId) {

        return chatService
                .createdPrivateChat(userId);
    }

    @PostMapping("/group")
    public ChatEntity createGroupChat(
            @RequestParam String name,
            @RequestParam Long userId) {

        return chatService
                .createdGroupChat(name, userId);
    }

}
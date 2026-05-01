package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Models.GroupMember;
import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Repository.Chatrepository;
import com.chatapp.demo.Service.ChatService;
import com.chatapp.demo.Repository.Userrepository;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private Chatrepository chatRepository;

    @Autowired
    private Userrepository userRepository;

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

    @GetMapping("/chats")
    public List<ChatEntity> getUserChats(
            @RequestParam String email) {

        UserEntity user =
                userRepository
                .findByEmail(email);

        if (user == null) {
            return List.of();
        }
        
        return chatRepository
                .findChatsByUser(email);
    }

}
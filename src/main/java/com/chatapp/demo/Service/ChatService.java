package com.chatapp.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Repository.Chatrepository;

import java.time.LocalDateTime;

@Service
public class ChatService {
    
    @Autowired
    private Chatrepository chatRepository;

    public ChatEntity createdPrivateChat(Long userId){
        ChatEntity chat = new ChatEntity();
        chat.setChatType("private");
        chat.setChatName(null);
        chat.setCreatedBy(userId);
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    public ChatEntity createdGroupChat(String chatName, Long userId){
        ChatEntity chat = new ChatEntity();
        chat.setChatType("group");
        chat.setChatName(chatName);
        chat.setCreatedBy(userId);
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

}

package com.chatapp.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.demo.Repository.Messagerepository;


import java.time.LocalDateTime;
import com.chatapp.demo.Models.Message;

@Service
public class MessageService {
    
    @Autowired
    private Messagerepository messageRepository;

    public void sendMessage(Long chatId, Long senderId, String text){
        Message message = new Message();
        
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setMessageType("text");
        message.setMessageText(text);
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }

}

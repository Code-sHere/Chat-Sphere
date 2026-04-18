package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chatapp.demo.Service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam Long chatId,
            @RequestParam Long senderId,
            @RequestParam String text) {

        messageService.sendMessage(
                chatId,
                senderId,
                text
        );

        return "Message saved successfully";
    }
}
package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chatapp.demo.Models.Message;
import java.util.List;
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

        System.out.println("Controller hit!");

        messageService.sendMessage(
                chatId,
                senderId,
                text);

        return "Message saved successfully";
    }

    @ResponseBody
    @GetMapping("/messages")
    public List<Message> getMessage(
            @RequestParam String sender,
            @RequestParam String receiver) {
        return messageService.getMessagesBetweenUsers(sender, receiver);
    }

}
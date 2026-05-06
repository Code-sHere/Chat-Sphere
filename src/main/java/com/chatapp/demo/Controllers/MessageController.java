package com.chatapp.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chatapp.demo.Models.Message;
import com.chatapp.demo.Service.MessageService;
import com.chatapp.demo.Repository.Messagerepository;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final Messagerepository messagerepository;

    @Autowired
    private MessageService messageService;

    MessageController(Messagerepository messagerepository) {
        this.messagerepository = messagerepository;
    }

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

    @GetMapping("/messages")
    public List<Message> getMessage(
            @RequestParam String sender,
            @RequestParam String receiver) {

        return messageService.getMessagesBetweenUsers(sender, receiver);
    }

    @PutMapping("/seen")
    public void seenMessage(
            @RequestParam Long chatId) {

        List<Message> messages =
                messagerepository
                        .findByChatId(chatId);

        for (Message msg : messages) {
            msg.setSeen(true);
        }

        messagerepository.saveAll(messages);
    }
}
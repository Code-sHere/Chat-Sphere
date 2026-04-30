package com.chatapp.demo.Controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/group")
public class GroupChatController {
    
    @PostMapping("/create")
    public String createRoom(){
        String roomId = UUID.randomUUID().toString().substring(0,8);

        return roomId;
    }
}

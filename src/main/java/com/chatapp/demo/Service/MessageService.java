package com.chatapp.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatapp.demo.Repository.Messagerepository;
import com.chatapp.demo.Repository.Chatrepository;

import com.chatapp.demo.Models.Message;   
import com.chatapp.demo.Repository.Userrepository;

import java.util.List;

import java.time.LocalDateTime;

import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Service.MessageService;
import com.chatapp.demo.Models.UserEntity;

@Service
public class MessageService {

    @Autowired
    private Messagerepository messageRepository;

    @Autowired
    private Userrepository userrepository;

    @Autowired
    private Chatrepository chatrepository;

    public void sendMessage(Long chatId, Long senderId, String text) {
        Message message = new Message();

        System.out.println("Saving message...");

        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setMessageType("text");
        message.setMessageText(text);
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);
        System.out.println(chatId);
        System.out.println(senderId);
        System.out.println(text);
        System.out.println("Message saved!");
    }

    public List<Message> getMessagesBetweenUsers(
            String senderEmail,
            String receiverEmail) {

        UserEntity sender = userrepository.findByEmail(senderEmail);

        UserEntity receiver = userrepository.findByEmail(receiverEmail);

        String chatName1 = sender.getId() + "-" +
                receiver.getId();

        String chatName2 = receiver.getId() + "-" +
                sender.getId();

        String chatName = "-" + receiver.getId();

        ChatEntity chat = chatrepository.findByChatName(chatName1);

        if (chat == null) {

            chat = chatrepository.findByChatName(chatName2);

        }

        if (chat == null) {

            return List.of();

        }

        return messageRepository
                .findByChatId(
                        chat.getId());
    }

}

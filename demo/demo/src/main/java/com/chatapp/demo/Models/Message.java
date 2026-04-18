package com.chatapp.demo.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table( name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Message(){
    }

    public Message(Long chatId,
                   Long senderId,
                   String messageType,
                   String messageText,
                   LocalDateTime createdAt) {

        this.chatId = chatId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.messageText = messageText;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }

    public Long getChatId(){
        return chatId;
    }

    public void setChatId(Long chatId){
        this.chatId = chatId;
    }

    public Long getSenderId(){
        return senderId;
    }

    public void setSenderId(Long senderId){
        this.senderId = senderId;
    }

    public String getMessageType(){
        return messageType;
    }

    public void setMessageType(String messageType){
        this.messageType = messageType;
    }

    public String getMessageText(){
        return messageText;
    }
    public void setMessageText(String messageText){
        this.messageText = messageText;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    

}

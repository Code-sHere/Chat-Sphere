package com.chatapp.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "chats")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_type")
    private String chatType;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ChatEntity() {
    }

    public ChatEntity(String chatType,
                      String chatName,
                      Long createdBy,
                      LocalDateTime createdAt) {

        this.chatType = chatType;
        this.chatName = chatName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
package com.chatapp.demo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_messages") 
public class GroupMessageEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long groupId;
    private String senderEmail;
    private String text;
    private String timestamp;

    public GroupMessageEntity(){
    }

    public GroupMessageEntity(Long groupId, String senderEmail, String text, String timestamp){
        this.groupId = groupId;
        this.senderEmail = senderEmail;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getGroupId(){
        return groupId;
    }

    public void setGroupId(Long groupId){
        this.groupId = groupId;
    }

    public String getSenderEmail(){
        return senderEmail;
    }

    public void serSenderEmail(String senderEmail){
        this.senderEmail = senderEmail;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

}

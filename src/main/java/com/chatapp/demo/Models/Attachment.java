package com.chatapp.demo.Models;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    private Long id;
    private String fileName;
    private String fileType;
    private String fileUrl;
    private String fileSize;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Message message;

    public Attachment() {}

    public Long getId(){
        return id;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFileType(){
        return fileType;
    }

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public String getFileUrl(){
        return fileUrl;
    }

    public void setFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }

    public String getFileSize(){
        return fileSize;
    }

    public void setFileSize(String fileSize){
        this.fileSize = fileSize;
    }

    public Message getMessage(){
        return message;
    }

    public void setMessage(Message message){
        this.message = message;
    }
}

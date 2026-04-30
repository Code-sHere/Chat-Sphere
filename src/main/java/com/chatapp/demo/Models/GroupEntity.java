package com.chatapp.demo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "groupchats")
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String groupName;
    private Long createdBy;
    private LocalDateTime createdAt;

    public GroupEntity(){
    }

    public GroupEntity(String groupName, Long createdBy, LocalDateTime createdAt) {
        this.groupName = groupName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId(){
        return id;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public Long getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(Long createdBy){
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

}

package com.chatapp.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatapp.demo.Models.Attachment;

public interface Attachmentrepository
        extends JpaRepository<Attachment, Long> {
}
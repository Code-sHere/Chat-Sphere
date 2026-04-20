package com.chatapp.demo.Repository;

import org.springframework.stereotype.Repository;

import com.chatapp.demo.Models.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface Chatrepository extends JpaRepository<ChatEntity, Long> {
}

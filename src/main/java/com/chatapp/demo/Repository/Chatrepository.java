package com.chatapp.demo.Repository;

import org.springframework.stereotype.Repository;
import java.util.List;

import com.chatapp.demo.Models.Message;
import com.chatapp.demo.Models.ChatEntity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface Chatrepository extends JpaRepository<ChatEntity, Long> {

    ChatEntity findByChatName(String chatName);


}

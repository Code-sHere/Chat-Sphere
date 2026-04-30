package com.chatapp.demo.Repository;

import org.springframework.stereotype.Repository;
import java.util.List;

import com.chatapp.demo.Models.Message;
import com.chatapp.demo.Models.ChatEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface Chatrepository extends JpaRepository<ChatEntity, Long> {

    
    ChatEntity findByChatName(String chatName);

    @Query("""
    SELECT c FROM ChatEntity c
    WHERE c.chatName LIKE CONCAT('%', :email, '%')
    ORDER BY c.createdAt DESC
    """)
    List<ChatEntity> findChatsByUser(
            @Param("email")
            String email);


}

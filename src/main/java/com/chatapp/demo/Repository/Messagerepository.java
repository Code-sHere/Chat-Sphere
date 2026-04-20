package com.chatapp.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapp.demo.Models.Message;


@Repository
public interface Messagerepository extends JpaRepository<Message, Long> {
}


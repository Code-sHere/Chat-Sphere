package com.chatapp.demo;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.chatapp.demo.config.SpringConfigurator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Repository.Chatrepository;
import com.chatapp.demo.Repository.Userrepository;
import com.chatapp.demo.Service.MessageService;

@Component
@ServerEndpoint(value = "/private/{username}", configurator = SpringConfigurator.class)
public class PrivateMessageServer {

        @Autowired
        private MessageService messageService;

        @Autowired
        private Userrepository userrepository;

        @Autowired
        private Chatrepository chatrepository;

        private Long getUserIdByUsername(String username) {

                UserEntity user = userrepository.findByEmail(username);
                if (user == null) {
                        throw new RuntimeException(
                                        "User not found: " + username);
                }

                return user.getId();
        }

        private Long getOrCreateChat(
                        Long senderId,
                        String receiverName) {

                UserEntity receiver = userrepository.findByEmail(receiverName);

                String chatName = senderId + "-" + receiver.getId();

                ChatEntity chat = chatrepository.findByChatName(chatName);

                if (chat == null) {

                        chat = new ChatEntity();

                        chat.setChatType("PRIVATE");

                        chat.setChatName(
                                        senderId + "-" + receiver.getId());

                        chat.setCreatedBy(senderId);

                        chat.setCreatedAt(
                                        LocalDateTime.now());

                        chatrepository.save(chat);

                }

                return chat.getId();

        }

        private static Map<String, Session> usersDirectory = new ConcurrentHashMap<>();

        @OnOpen
        public void onOpen(Session session,
                        @PathParam("username") String username) {

                usersDirectory.put(username, session);

                session.getUserProperties()
                                .put("username", username);

                System.out.println("User registered: "
                                + username);

                System.out.println("Connected users: "
                                + usersDirectory.keySet());
                boradcastOnlineUsers();
        }

        @OnMessage
        public void onMessage(Session session, String message) throws IOException {

                String sender = (String) session.getUserProperties().get("username");

                if (message.startsWith("Typing:")) {

                        String receiver = message.replace("Typing:", "").trim();

                        Session typingSession = usersDirectory.get(receiver);

                        if (typingSession != null && typingSession.isOpen()) {
                                typingSession.getBasicRemote().sendText("Typing:" + sender);
                        }

                        return;
                }

                
                String[] parts = message.trim().split(":", 2);

                if (parts.length != 2) {
                        sendMessage(session, "Invalid format");
                        return;
                }

                String receiver = parts[0].trim();
                String text = parts[1].trim();

                Session receiverSession = usersDirectory.get(receiver);

                if (receiverSession != null && receiverSession.isOpen()) {
                        sendMessage(receiverSession, sender + ":" + text);
                }
        }

        @OnClose
        public void onClose(Session session) {

                String username = (String) session
                                .getUserProperties()
                                .get("username");

                usersDirectory.remove(username);

                System.out.println(
                                username + " disconnected");

                boradcastOnlineUsers();
        }

        private void sendMessage(
                        Session session,
                        String message) {

                try {

                        session.getBasicRemote()
                                        .sendText(message);

                } catch (IOException e) {

                        e.printStackTrace();

                }
        }

        private void boradcastOnlineUsers() {
                String users = String.join(",", usersDirectory.keySet());

                for (Session session : usersDirectory.values()) {
                        try {
                                session.getBasicRemote().sendText("Online users: " + users);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
}
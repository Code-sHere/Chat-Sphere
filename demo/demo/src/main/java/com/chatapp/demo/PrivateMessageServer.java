package com.chatapp.demo;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chatapp.demo.Service.MessageService;

@Component
@ServerEndpoint(
        value = "/private/{username}",
        configurator = SpringConfigurator.class
)
public class PrivateMessageServer {

    @Autowired
    private MessageService messageService;

    private static Map<String, Session> usersDirectory =
            new ConcurrentHashMap<>();

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
    }

    @OnMessage
    public void onMessage(Session session,
                          String message) {

        String sender =
                (String) session
                        .getUserProperties()
                        .get("username");

        String[] parts =
                message.trim().split(":", 2);

        if (parts.length != 2) {
            sendMessage(session,
                    "Invalid format. Use receiver:message");
            return;
        }

        String receiver = parts[0].trim();
        String text = parts[1].trim();

        if (sender.equals(receiver)) {
            sendMessage(session,
                    "You can't send message to yourself");
            return;
        }

        System.out.println("Sender: " + sender);
        System.out.println("Receiver: " + receiver);
        System.out.println("Message: " + text);

        Session receiverSession =
                usersDirectory.get(receiver);

        /*
         TEMPORARY VALUES
         Later we will fetch real IDs from DB
        */

        Long chatId = 1L;
        Long senderId = 1L;

        /*
         SAVE MESSAGE TO DATABASE
        */

        messageService.sendMessage(
                chatId,
                senderId,
                text
        );

        /*
         SEND MESSAGE TO RECEIVER
        */

        if (receiverSession != null
                && receiverSession.isOpen()) {

            sendMessage(
                    receiverSession,
                    "Message from "
                            + sender
                            + ": "
                            + text
            );

        } else {

            sendMessage(
                    session,
                    "User "
                            + receiver
                            + " is not online"
            );
        }
    }

    @OnClose
    public void onClose(Session session) {

        String username =
                (String) session
                        .getUserProperties()
                        .get("username");

        usersDirectory.remove(username);

        System.out.println(
                username + " disconnected"
        );
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
}
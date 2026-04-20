package com.chatapp.demo;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/chat")
public class ChatServer {
    private static Set<Session> clients = new CopyOnWriteArraySet<>();
    private static Map<Session, String> users = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session){
        clients.add(session);
       try {
        session.getBasicRemote().sendText("Enter your Username: ");

       } catch (IOException e) {
            e.printStackTrace();
       }
    }

    @OnMessage
    public void onMessage(String message, Session sender)
    throws IOException{
        
        if(!users.containsKey(sender)){
            users.put(sender,message);
            broadcast(message + " : joined the chat" );
            return;
        }

        String username = users.get(sender);

        String fullmessage = username + ": "+ message;
        broadcast(fullmessage);
    }

    @OnClose
    public void onClose(Session session)
    throws IOException{
        String username = users.get(session);

        clients.remove(session);
        users.remove(session);

        if(username != null){
            broadcast (username + " left the room");
            }
        }

    public void broadcast(String message)
    throws IOException{
        for(Session client : clients){
            if(client.isOpen()){
                client.getBasicRemote().sendText(message);
            }
        }
    }
}

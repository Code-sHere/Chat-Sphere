package com.chatapp.demo;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import jakarta.websocket.server.PathParam;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/private/{username}")
public class PrivateMessageServer {

    private static Map<String, Session> usersDirectory  = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        usersDirectory.put(username, session);  
            session.getUserProperties().put("username", username);

            System.out.println("user registerd : "+ username);
        System.out.println("Connected users: " + usersDirectory.keySet());
    }
    @OnMessage
    public void onMessage(Session session, String message){
        String sender = (String) session.getUserProperties().get("username");
        
        String[] parts = message.trim().split(":", 2);

        if(parts.length != 2){
            try{
                session.getBasicRemote().sendText("Invalid message format. Use 'receiver:message'");
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return;
        }

        String receiver = parts[0].trim();
        String text = parts[1].trim();

        if(sender.equals(receiver)){
            try{
                session.getBasicRemote().sendText("You can't send message to yourself");
            }catch(IOException e){
                e.printStackTrace();
            }
            return;
        }
        System.out.println("Sender: " + sender);
        System.out.println("Receiver: " + receiver);
        System.out.println("Available users: " + usersDirectory.keySet());
        System.out.println("RAW MESSAGE: " + message);

        Session receiverSession = usersDirectory.get(receiver);

        if(receiverSession !=null && receiverSession.isOpen()){
            try{
                receiverSession.getBasicRemote().sendText("Message from " + sender + ":" + text);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            try{
                session.getBasicRemote().sendText("User " + receiver + " is not online");
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        
            
    }

    @OnClose
    public void onClose(Session session){
        String username = (String) session.getUserProperties().get("username");
        usersDirectory.remove(username);
    }


}

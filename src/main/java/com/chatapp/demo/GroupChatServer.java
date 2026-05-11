package com.chatapp.demo;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/group/{groupId}/{username}")
public class GroupChatServer {
    private static Map<String, Set<Session>> groups = new ConcurrentHashMap<>();
    private static Map<Session, String> users = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(
        Session session,
        @PathParam("groupId") String groupId,
        @PathParam("username") String username
    ){
        users.put(session, username);

        groups.computeIfAbsent(groupId, k -> new CopyOnWriteArraySet<>()).add(session);

        try{
            broadcast(groupId,"System:" + username + " Joined the Group");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(
        String message,
        Session sender,
        @PathParam("groupId") String groupId
    ) throws IOException{
        String username = users.get(sender);

        if(username == null){
            return;
        }

        if(message.startsWith("Typing:")){
            Set<Session> members = groups.get(groupId);

            if(members !=null ){
                for(Session client: members){
                    if(client != sender && client.isOpen()){

                        client.getAsyncRemote().sendText("Typing:" + username);
                    }
                }
            }
            return;
        }

        if(message.startsWith("{")){
            for(Session client: groups.get(groupId)){
                if(client.isOpen()){
                    client.getBasicRemote().sendText(message);
                }
            }
            return;
        }

        broadcast(groupId,"Chat:" + username + ": " + message);
   }

    @OnClose
    public void onClose(
        Session session,
        @PathParam("groupId") String groupId
    ) throws IOException{
        String username = users.remove(session);

       Set<Session> members = groups.get(groupId);

       if(members != null){
        members.remove(session);

        if(members.isEmpty()){
            groups.remove(groupId);
        }

       }

       if(username != null){
        broadcast(groupId,"System:" + username + " left the Group");
       }

    }


    public void broadcast(String groupId, String message)
    throws IOException{

        Set<Session> members = groups.get(groupId);

        if(members == null){
            return;
        }
        for(Session client : members){
            if(client.isOpen()){
                client.getBasicRemote().sendText(message);
            }
        }
    }
}

package com.chatapp.demo.Service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


import com.chatapp.demo.Controllers.UserController;
import com.chatapp.demo.Models.ChatEntity;
import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Repository.Userrepository;

@Service
public class Userservice {
    
    @Autowired
    private Userrepository userrepository;

    public UserEntity login(String email, String password){
        UserEntity user = userrepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void saveUser(UserEntity user) {
        userrepository.saveUser(user);
    }

    public List<UserEntity> getAllUsers(){
        return userrepository.findAllUsers();
    }

    
    
}

package com.chatapp.demo.Service;

import com.chatapp.demo.Models.UserEntity;
import com.chatapp.demo.Repository.Userrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private Userrepository userrepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
         System.out.println("EMAIL RECEIVED: [" + email + "]");

        UserEntity user = userrepository.findByEmail(email);

        System.out.println("User found: [" + user.getEmail() + "]");

        System.out.println("user found: [" + user.getPassword() + "]");

        return new User(
            user.getEmail(),
            user.getPassword(),
            new ArrayList<>()  
             
        );

        
    }
    
}

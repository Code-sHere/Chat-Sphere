package com.chatapp.demo.Repository;

import java.util.List;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.repository.query.Query;
import org.springframework.jdbc.core.RowMapper;

import com.chatapp.demo.Models.UserEntity;

@Repository
public class Userrepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUser(UserEntity user) {

        String sql = "INSERT INTO users (name, email, password, about) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAbout()

        );

    }

    public UserEntity findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        var users = jdbcTemplate.query(
                sql,
                new Object[] { email },
                (rs, rowNum) -> {
                    UserEntity user = new UserEntity();

                    user.setId(rs.getLong("id")); 
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setAbout(rs.getString("about"));
                    return user;
                });

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public List<UserEntity> findAllUsers(){
        String sql = "SELECT id, name, email, password, about From users";

        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            UserEntity user = new UserEntity();

            user.setId(rs.getLong("id")); 
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setAbout(rs.getString("about"));
            return user;
        });
    }

}

package com.chatapp.demo.Models;

public class UserEntity {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String about;

    public UserEntity() {
    }

    public UserEntity(Long id,
                      String username,
                      String email,
                      String password,
                      String about) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
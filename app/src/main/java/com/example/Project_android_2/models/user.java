package com.example.Project_android_2.models;

public class user {
    private int id;
    private String avatar;
    private String email;
    private String password;
    private String username;
    private String uid;

    public user() {

    }

    public user(int id, String avatar, String email, String password, String username, String uid) {
        this.id = id;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
        this.username = username;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

package com.example.Project_android_2.models;

import java.util.Date;

public class reset_password {
    private int user_id;
    private String token;
    private Date createAt;
    private boolean isValidate;

    public reset_password() {

    }

    public reset_password(int user_id, String token, Date createAt, boolean isValidate) {
        this.user_id = user_id;
        this.token = token;
        this.createAt = createAt;
        this.isValidate = isValidate;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public void setValidate(boolean validate) {
        isValidate = validate;
    }
}

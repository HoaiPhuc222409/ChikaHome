package com.example.chikaapp.request;

public class LoginRequest {

    private String phoneOrEmail;

    private String password;

    public LoginRequest(String phoneOrEmail, String password) {
        this.phoneOrEmail = phoneOrEmail;
        this.password = password;
    }
}

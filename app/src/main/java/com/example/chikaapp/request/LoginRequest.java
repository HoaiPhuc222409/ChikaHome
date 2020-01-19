package com.example.chikaapp.request;

public class LoginRequest {

    private String usernameOrEmail;

    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}

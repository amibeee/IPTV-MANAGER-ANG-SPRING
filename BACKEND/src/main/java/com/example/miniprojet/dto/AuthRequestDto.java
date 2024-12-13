package com.example.miniprojet.dto;

import java.io.Serializable;

public class AuthRequestDto implements Serializable {
    private String username;
    private String password;

    // Default constructor
    public AuthRequestDto() {
    }

    // Parameterized constructor
    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


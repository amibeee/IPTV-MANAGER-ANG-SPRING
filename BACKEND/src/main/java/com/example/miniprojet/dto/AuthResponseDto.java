package com.example.miniprojet.dto;

import java.io.Serializable;

public class AuthResponseDto implements Serializable {
    private final String token;
    private final String role;

    public AuthResponseDto(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}

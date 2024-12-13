package com.example.miniprojet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;
    private String password; // Only used for registration/update
    private boolean subscriptionActive = false;
    private LocalDateTime subscriptionExpiry;
    private String subscriptionTier;
    private BigDecimal accountBalance = BigDecimal.ZERO;
    private boolean active;
    private String role;
}


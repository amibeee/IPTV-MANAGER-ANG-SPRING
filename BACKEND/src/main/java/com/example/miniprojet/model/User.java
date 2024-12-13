package com.example.miniprojet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean enabled = true;
    private boolean active;
    @Column(name = "subscription_active", nullable = false)
    private boolean subscriptionActive = false;

    @Column(name = "subscription_expiry")
    private LocalDateTime subscriptionExpiry;

    @Column(name = "subscription_tier")
    private String subscriptionTier;

    @Column(name = "account_balance")
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;
    @ManyToMany
    @JoinTable(
            name = "user_purchased_channels",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )

    private Set<Channel> purchasedChannels = new HashSet<>();

    // Method to add a purchased channel
    public void purchaseChannel(Channel channel) {
        if (this.accountBalance.compareTo(channel.getPrice()) >= 0) {
            this.purchasedChannels.add(channel);
            this.accountBalance = this.accountBalance.subtract(channel.getPrice());
        } else {
            throw new IllegalStateException("Insufficient balance to purchase the channel");
        }
    }

    public boolean hasPurchasedChannel(Channel channel) {
        return this.purchasedChannels.contains(channel);
    }


    // Add this method to the existing User class
    public void addFundsToAccount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.accountBalance = this.accountBalance.add(amount);
        } else {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
    // Important for Spring Security
    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
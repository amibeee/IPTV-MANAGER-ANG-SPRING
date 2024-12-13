package com.example.miniprojet.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Channel name is required")
    private String name;

    private String streamUrl;
    private String logo;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_purchasable", nullable = false)
    private boolean purchasable = true;

    @ManyToMany(mappedBy = "purchasedChannels")
    private Set<User> purchasedByUsers;
}
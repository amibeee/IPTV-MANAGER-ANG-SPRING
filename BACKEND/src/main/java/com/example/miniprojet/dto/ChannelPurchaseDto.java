package com.example.miniprojet.dto;

import com.example.miniprojet.model.Channel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class ChannelPurchaseDto {
    private Long channelId;
    private String channelName;
    private BigDecimal price;
    private boolean purchased;
    private LocalDateTime timestamp; // Add timestamp field

    public ChannelPurchaseDto() {}

    public ChannelPurchaseDto(Channel channel, boolean purchased) {
        this.channelId = channel.getId();
        this.channelName = channel.getName();
        this.price = channel.getPrice();
        this.purchased = purchased;
        this.timestamp = timestamp; // Set timestamp


    }

    // Getters and Setters
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
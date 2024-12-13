package com.example.miniprojet.controller;

import com.example.miniprojet.dto.ChannelPurchaseDto;
import com.example.miniprojet.service.ChannelPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/channels/purchase")
public class ChannelPurchaseController {
    @Autowired
    private ChannelPurchaseService channelPurchaseService;

    /**
     * Purchase a channel for a user
     * @param userId User ID
     * @param channelId Channel ID to purchase
     * @return Purchased ChannelPurchaseDto
     */
    @PostMapping
    public ResponseEntity<ChannelPurchaseDto> purchaseChannel(
            @RequestParam Long userId,
            @RequestParam Long channelId
    ) {
        ChannelPurchaseDto purchase = channelPurchaseService.purchaseChannel(userId, channelId);
        purchase.setTimestamp(LocalDateTime.now()); // Set the current time as the timestamp
        return ResponseEntity.ok(purchase);
    }

    /**
     * Get list of purchasable channels for a user
     * @param userId User ID to check channels for
     * @return List of ChannelPurchaseDto
     */
    @GetMapping("/available")
    public ResponseEntity<List<ChannelPurchaseDto>> getPurchasableChannels(
            @RequestParam Long userId
    ) {
        List<ChannelPurchaseDto> channels = channelPurchaseService.getPurchasableChannels(userId);
        channels.forEach(channel -> channel.setTimestamp(LocalDateTime.now())); // Set timestamp for each channel
        return ResponseEntity.ok(channels);
    }

    /**
     * Get user's purchased channels
     * @param userId User ID
     * @return List of purchased ChannelPurchaseDto
     */
    @GetMapping("/my-channels")
    public ResponseEntity<List<ChannelPurchaseDto>> getUserPurchasedChannels(
            @RequestParam Long userId
    ) {
        List<ChannelPurchaseDto> channels = channelPurchaseService.getUserPurchasedChannels(userId);
        return ResponseEntity.ok(channels);
    }

    /**
     * Add funds to user's account
     * @param userId User ID
     * @param amount Amount to add
     * @return Updated account balance
     */
    @PostMapping("/add-funds")
    public ResponseEntity<BigDecimal> addFundsToAccount(
            @RequestParam Long userId,
            @RequestParam BigDecimal amount
    ) {
        BigDecimal balance = channelPurchaseService.addFundsToAccount(userId, amount);
        return ResponseEntity.ok(balance);
    }
}
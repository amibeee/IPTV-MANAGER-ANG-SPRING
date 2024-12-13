package com.example.miniprojet.service;

import com.example.miniprojet.dto.ChannelPurchaseDto;
import com.example.miniprojet.model.Channel;
import com.example.miniprojet.model.User;
import com.example.miniprojet.repository.ChannelRepository;
import com.example.miniprojet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelPurchaseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    /**
     * Purchase a channel for a user
     * @param userId User purchasing the channel
     * @param channelId Channel to be purchased
     * @return ChannelPurchaseDto with purchase details
     */
    @Transactional
    public ChannelPurchaseDto purchaseChannel(Long userId, Long channelId) {
        // Find user and channel
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        // Check if channel is purchasable
        if (!channel.isPurchasable()) {
            throw new RuntimeException("Channel is not available for purchase");
        }

        // Check if user already owns the channel
        if (user.hasPurchasedChannel(channel)) {
            throw new RuntimeException("User already owns this channel");
        }

        // Attempt to purchase
        user.purchaseChannel(channel);

        // Save updated user
        userRepository.save(user);

        // Return purchase details
        return new ChannelPurchaseDto(channel, true);
    }

    /**
     * Get list of purchasable channels for a user
     * @param userId User to check channels for
     * @return List of ChannelPurchaseDto
     */
    public List<ChannelPurchaseDto> getPurchasableChannels(Long userId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get all purchasable channels
        List<Channel> purchasableChannels = channelRepository.findByPurchasableTrue();

        // Map channels to DTOs with purchase status
        return purchasableChannels.stream()
                .map(channel -> new ChannelPurchaseDto(
                        channel,
                        user.hasPurchasedChannel(channel)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Add funds to user's account
     * @param userId User ID
     * @param amount Amount to add
     * @return Updated account balance
     */
    @Transactional
    public BigDecimal addFundsToAccount(Long userId, BigDecimal amount) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add funds
        user.addFundsToAccount(amount);

        // Save updated user
        userRepository.save(user);

        // Return new balance
        return user.getAccountBalance();
    }

    /**
     * Get user's purchased channels
     * @param userId User ID
     * @return List of purchased ChannelPurchaseDto
     */
    public List<ChannelPurchaseDto> getUserPurchasedChannels(Long userId) {
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get purchased channels
        return user.getPurchasedChannels().stream()
                .map(channel -> new ChannelPurchaseDto(channel, true))
                .collect(Collectors.toList());
    }
}
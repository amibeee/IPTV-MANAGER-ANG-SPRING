package com.example.miniprojet.service;
import com.example.miniprojet.dto.ChannelDto;
import com.example.miniprojet.model.Category;
import com.example.miniprojet.model.Channel;
import com.example.miniprojet.repository.CategoryRepository;
import com.example.miniprojet.repository.ChannelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ChannelDto createChannel(ChannelDto channelDto) {
        Channel channel = new Channel();
        BeanUtils.copyProperties(channelDto, channel);

        // Set category if category ID is provided
        if (channelDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(channelDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            channel.setCategory(category);
        }

        // Save channel
        channel = channelRepository.save(channel);

        // Update DTO with saved channel ID
        channelDto.setId(channel.getId());
        return channelDto;
    }

    @Transactional(readOnly = true)
    public List<ChannelDto> getAllChannels() {
        return channelRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ChannelDto> getChannelById(Long id) {
        return channelRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<ChannelDto> getChannelsByCategory(Long categoryId) {
        return channelRepository.findByCategory_Id(categoryId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChannelDto updateChannel(Long id, ChannelDto channelDto) {
        Channel existingChannel = channelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        // Update fields
        existingChannel.setName(channelDto.getName());
        existingChannel.setStreamUrl(channelDto.getStreamUrl());
        existingChannel.setLogo(channelDto.getLogo());
        existingChannel.setActive(channelDto.isActive());

        // Update category if provided
        if (channelDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(channelDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingChannel.setCategory(category);
        }

        Channel updatedChannel = channelRepository.save(existingChannel);

        return convertToDto(updatedChannel);
    }

    @Transactional
    public void deleteChannel(Long id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        channelRepository.delete(channel);
    }

    private ChannelDto convertToDto(Channel channel) {
        ChannelDto dto = new ChannelDto();
        BeanUtils.copyProperties(channel, dto);

        // Set category ID
        if (channel.getCategory() != null) {
            dto.setCategoryId(channel.getCategory().getId());
        }

        return dto;
    }
}
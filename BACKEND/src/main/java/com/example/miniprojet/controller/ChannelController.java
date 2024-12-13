package com.example.miniprojet.controller;
import com.example.miniprojet.dto.ChannelDto;
import com.example.miniprojet.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    public ResponseEntity<ChannelDto> createChannel(@Valid @RequestBody ChannelDto channelDto) {
        ChannelDto createdChannel = channelService.createChannel(channelDto);
        return new ResponseEntity<>(createdChannel, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllChannels() {
        List<ChannelDto> channels = channelService.getAllChannels();
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelDto> getChannelById(@PathVariable Long id) {
        return channelService.getChannelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ChannelDto>> getChannelsByCategory(@PathVariable Long categoryId) {
        List<ChannelDto> channels = channelService.getChannelsByCategory(categoryId);
        return ResponseEntity.ok(channels);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChannelDto> updateChannel(
            @PathVariable Long id,
            @Valid @RequestBody ChannelDto channelDto
    ) {
        ChannelDto updatedChannel = channelService.updateChannel(id, channelDto);
        return ResponseEntity.ok(updatedChannel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }
}

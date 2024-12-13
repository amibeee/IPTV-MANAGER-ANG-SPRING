package com.example.miniprojet.service;

import com.example.miniprojet.dto.CategoryDto;
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
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);

        // Save category
        category = categoryRepository.save(category);

        // Update DTO with saved category ID
        categoryDto.setId(category.getId());
        return categoryDto;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update fields
        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);

        return convertToDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }

    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        BeanUtils.copyProperties(category, dto);

        // Convert channels if needed
        if (category.getChannels() != null) {
            dto.setChannels(category.getChannels().stream()
                    .map(this::convertChannelToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private ChannelDto convertChannelToDto(Channel channel) {
        ChannelDto dto = new ChannelDto();
        BeanUtils.copyProperties(channel, dto);

        // Set category ID
        if (channel.getCategory() != null) {
            dto.setCategoryId(channel.getCategory().getId());
        }

        return dto;
    }
}
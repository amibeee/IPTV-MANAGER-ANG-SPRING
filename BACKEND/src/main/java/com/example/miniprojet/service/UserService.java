package com.example.miniprojet.service;

import com.example.miniprojet.dto.UserCreationDto;
import com.example.miniprojet.dto.UserDto;
import com.example.miniprojet.model.User;
import com.example.miniprojet.repository.UserRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(UserCreationDto userCreationDto) {
        // Check if username or email already exists
        if (userRepository.findByUsername(userCreationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userCreationDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        BeanUtils.copyProperties(userCreationDto, user);

        // Set default values and encode password
        user.setActive(true);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));

        user = userRepository.save(user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
    @Transactional(readOnly = true)
    public String getUserRole(Long id) {
        return userRepository.findById(id)
                .map(User::getRole)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update allowed fields
        existingUser.setEmail(userDto.getEmail());
        existingUser.setActive(userDto.isActive());
        existingUser.setRole(userDto.getRole());

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public User registerNewUser(UserDto userDto) {
        // Create a new user based on the DTO
        User newUser = new User();
        BeanUtils.copyProperties(userDto, newUser);

        // Encode the password
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Set default values
        newUser.setActive(true);
        newUser.setRole("ROLE_USER");

        // Save and return the user
        return userRepository.save(newUser);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        // Optionally, hide the password
        dto.setPassword(null);
        return dto;
    }
}
package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.UserDTO;
import com.example.demo.DTOs.UserCompleteDTO;
import com.example.demo.entities.*;
import com.example.demo.exceptions.UsernameNotFoundException;
import com.example.demo.repositories.*;



@Service
public class UserServices{
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<UserDTO> findAllusers() {
        List<UserDTO> users = userRepository.findAll().stream().map(
            user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail())
        ).collect(Collectors.toList());
        return users;
    }
    
    
    // Get user by id
    public Optional<UserDTO> getUserById(Long id) {
        Optional<UserDTO> userInfo = userRepository.findById(id).map(
            user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail())
        );
        return userInfo;
    }


    // update user
    public User updateUser(Long id, UserCompleteDTO userCompleteDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(userCompleteDTO.username());
        user.setEmail(userCompleteDTO.email());
        if (userCompleteDTO.password() != null && !userCompleteDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userCompleteDTO.password()));
        }

        return userRepository.save(user);
    }
    // Get User complete details
    public Optional<UserCompleteDTO> getUserCompleteById(Long id) {
        Optional<UserCompleteDTO> userInfo = userRepository.findById(id).map(
            user -> new UserCompleteDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword())
        );
        return userInfo;
    }

    // Get User by username
    public Optional<UserCompleteDTO> getUserCompleteByUsername(String username) {
        Optional<UserCompleteDTO> userInfo = userRepository.findByUsername(username).map(
            user -> new UserCompleteDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword())
        );
        return userInfo;
    }


    
    // Save User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Delete User by id
    public ResponseEntity<Object> deleteUser(long id) {
        userRepository.deleteById(id);
        return null;
    }


    // search users 
    public List<UserDTO> searchUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("No users found");
        }
        return users.stream().map(
            user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail())
        ).collect(Collectors.toList());
    }




}

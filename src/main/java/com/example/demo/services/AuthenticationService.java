package com.example.demo.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.LoginDTO;
import com.example.demo.DTOs.UserCompleteDTO;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signup(UserCompleteDTO userCompleteDTO) {
        User user = new User();
        user.setUsername(userCompleteDTO.username());
        user.setEmail(userCompleteDTO.email());
        user.setPassword(passwordEncoder.encode(userCompleteDTO.password()));
        return userRepository.save(user);
    }

    public User authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDTO.username(), 
                loginDTO.password()
                )
        );

        return userRepository.findByUsername(loginDTO.username()).orElseThrow();
    }
}


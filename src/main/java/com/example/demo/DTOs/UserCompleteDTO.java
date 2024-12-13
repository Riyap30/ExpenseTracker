package com.example.demo.DTOs;


public record UserCompleteDTO(
    Long id,
    String username,
    String email,
    String password
){}

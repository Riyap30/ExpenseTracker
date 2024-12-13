package com.example.demo.DTOs;

import java.util.Date;

public record GroupDTO(
    Long id,
    String name,
    Date date,
    long admin,
    String adminName
) {}

package com.example.demo.DTOs;

import java.util.Date;


public record ExpenseDTO(
    long id,
    Double amount,
    String description,
    Date date,
    long userId,
    String categoryName
) {}

package com.example.demo.DTOs;

public record SplitDTO(
    long id,
    Double amount,
    long groupId,
    long payerId,
    String payerName,
    long payeeId,
    String payeeName,
    String categoryName,
    Boolean settled,
    String status

) {}

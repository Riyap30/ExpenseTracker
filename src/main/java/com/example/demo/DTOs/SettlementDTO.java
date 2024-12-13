package com.example.demo.DTOs;

import java.util.Date;

public record SettlementDTO(
    long id,
    Double amount,
    Date settledDate,
    long groupId,
    long payerId,
    String payerName,
    long receiverId,
    String receiverName
) {}

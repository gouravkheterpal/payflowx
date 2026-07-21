package com.gourav.payflowx.dto.response;

import com.gourav.payflowx.common.enums.TransactionStatus;
import com.gourav.payflowx.common.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionResponseDto {

    private UUID id;

    private UUID walletId;

    private BigDecimal amount;

    private TransactionType type;

    private TransactionStatus status;

    private String reference;

    private String description;

    private LocalDateTime createdAt;
}
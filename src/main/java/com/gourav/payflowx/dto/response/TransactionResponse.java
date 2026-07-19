package com.gourav.payflowx.dto.response;

import com.gourav.payflowx.common.enums.TransactionStatus;
import com.gourav.payflowx.common.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class TransactionResponse {

    private UUID transactionId;

    private BigDecimal amount;

    private TransactionType type;

    private TransactionStatus status;

    private String reference;

    private String description;

    private LocalDateTime createdAt;
}
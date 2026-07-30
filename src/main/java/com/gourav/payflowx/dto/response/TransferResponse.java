package com.gourav.payflowx.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponse {

    private String transactionId;

    private String senderEmail;

    private String receiverEmail;

    private BigDecimal amount;

    private String status;

    private LocalDateTime transferredAt;
}

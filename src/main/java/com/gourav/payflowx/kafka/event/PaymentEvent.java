package com.gourav.payflowx.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private String transactionId;

    private String senderEmail;

    private String receiverEmail;

    private BigDecimal amount;

    private String status;

    private LocalDateTime transferredAt;
}
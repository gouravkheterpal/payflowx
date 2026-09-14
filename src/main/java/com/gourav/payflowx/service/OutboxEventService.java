package com.gourav.payflowx.service;

import com.gourav.payflowx.entity.OutboxEvent;

public interface OutboxEventService {

    OutboxEvent createPaymentEvent(
            String transactionReference,
            String senderEmail,
            String receiverEmail,
            java.math.BigDecimal amount,
            String description
    );
}
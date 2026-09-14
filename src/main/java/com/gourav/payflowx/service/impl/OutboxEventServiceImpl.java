package com.gourav.payflowx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gourav.payflowx.entity.OutboxEvent;
import com.gourav.payflowx.repository.OutboxEventRepository;
import com.gourav.payflowx.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxEventServiceImpl implements OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public OutboxEvent createPaymentEvent(
            String transactionReference,
            String senderEmail,
            String receiverEmail,
            BigDecimal amount,
            String description) {

        Map<String, Object> event = new HashMap<>();

        event.put("transactionId", transactionReference);
        event.put("senderEmail", senderEmail);
        event.put("receiverEmail", receiverEmail);
        event.put("amount", amount);
        event.put("description", description);
        event.put("eventType", "PAYMENT_TRANSFERRED");

        final String payload;

        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to serialize payment event", e
            );
        }

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .aggregateType("PAYMENT")
                .aggregateId(transactionReference)
                .eventType("PAYMENT_TRANSFERRED")
                .payload(payload)
                .status("PENDING")
                .retryCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        return outboxEventRepository.save(outboxEvent);
    }
}
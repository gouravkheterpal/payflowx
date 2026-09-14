package com.gourav.payflowx.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "payflowx.payment.events",
            groupId = "payflowx-payment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String payload) {

        try {

            JsonNode event = objectMapper.readTree(payload);

            String transactionId =
                    event.get("transactionId").asText();

            String senderEmail =
                    event.get("senderEmail").asText();

            String receiverEmail =
                    event.get("receiverEmail").asText();

            BigDecimal amount =
                    event.get("amount").decimalValue();

            String eventType =
                    event.get("eventType").asText();

            String description =
                    event.has("description")
                            ? event.get("description").asText()
                            : null;

            log.info(
                    "Payment event received. transactionId={}, eventType={}, sender={}, receiver={}, amount={}",
                    transactionId,
                    eventType,
                    senderEmail,
                    receiverEmail,
                    amount
            );

            /*
             * This is where downstream business processing would happen.
             *
             * For now, we are deliberately keeping the consumer side-effect
             * free and using logging to prove successful event consumption.
             */

            log.info(
                    "Payment event processed successfully. transactionId={}, description={}",
                    transactionId,
                    description
            );

        } catch (Exception e) {

            log.error(
                    "Failed to process payment event. payload={}",
                    payload,
                    e
            );

            throw new IllegalStateException(
                    "Failed to process payment event",
                    e
            );
        }
    }
}
package com.gourav.payflowx.kafka.producer;

import com.gourav.payflowx.kafka.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private static final String PAYMENT_TOPIC = "payflowx.payment.events";

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public void publish(PaymentEvent event) {

        log.info(
                "Publishing payment event. transactionId={}, amount={}",
                event.getTransactionId(),
                event.getAmount()
        );

        kafkaTemplate.send(
                PAYMENT_TOPIC,
                event.getTransactionId(),
                event
        );
    }
}
package com.gourav.payflowx.kafka.consumer;

import com.gourav.payflowx.kafka.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentEventConsumer {

    @KafkaListener(
            topics = "payflowx.payment.events",
            groupId = "payflowx-payment-group"
    )
    public void consume(PaymentEvent event) {

        log.info(
                "Payment event consumed. transactionId={}, sender={}, receiver={}, amount={}, status={}",
                event.getTransactionId(),
                event.getSenderEmail(),
                event.getReceiverEmail(),
                event.getAmount(),
                event.getStatus()
        );
    }
}
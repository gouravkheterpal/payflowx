package com.gourav.payflowx.kafka.producer;

import com.gourav.payflowx.entity.OutboxEvent;
import com.gourav.payflowx.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

    private static final String PAYMENT_TOPIC = "payflowx.payment.events";

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxEventRepository
                        .findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent event : events) {

            try {

                kafkaTemplate.send(
                        PAYMENT_TOPIC,
                        event.getAggregateId(),
                        event.getPayload()
                ).get();

                event.setStatus("PUBLISHED");
                event.setPublishedAt(LocalDateTime.now());

                outboxEventRepository.save(event);

                log.info(
                        "Outbox event published: id={}, transaction={}",
                        event.getId(),
                        event.getAggregateId()
                );

            } catch (Exception e) {

                event.setRetryCount(
                        event.getRetryCount() + 1
                );

                outboxEventRepository.save(event);

                log.error(
                        "Failed to publish outbox event: id={}, retryCount={}",
                        event.getId(),
                        event.getRetryCount(),
                        e
                );
            }
        }
    }
}
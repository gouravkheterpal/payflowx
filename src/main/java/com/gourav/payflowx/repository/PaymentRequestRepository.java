package com.gourav.payflowx.repository;

import com.gourav.payflowx.entity.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRequestRepository
        extends JpaRepository<PaymentRequest, UUID> {

    Optional<PaymentRequest> findByIdempotencyKeyAndSenderId(
            String idempotencyKey,
            UUID senderId
    );
}
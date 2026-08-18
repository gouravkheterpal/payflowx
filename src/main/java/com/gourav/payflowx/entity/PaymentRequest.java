package com.gourav.payflowx.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.math.BigDecimal;


@Entity
@Table(name = "payment_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(nullable = false)
    private String transactionReference;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    private String receiverEmail;
}
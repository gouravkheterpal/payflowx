CREATE TABLE payment_requests (

    id UUID PRIMARY KEY,

    idempotency_key VARCHAR(100) NOT NULL,

    sender_id UUID NOT NULL,

    transaction_reference VARCHAR(100) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_payment_request_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id),

    CONSTRAINT uk_sender_idempotency
        UNIQUE(sender_id, idempotency_key)
);
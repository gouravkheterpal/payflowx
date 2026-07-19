CREATE TABLE transactions (
    id UUID PRIMARY KEY,

    wallet_id UUID NOT NULL,

    amount NUMERIC(19,2) NOT NULL,

    type VARCHAR(50) NOT NULL,

    status VARCHAR(50) NOT NULL,

    reference VARCHAR(100) NOT NULL UNIQUE,

    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_transactions_wallet
        FOREIGN KEY (wallet_id)
        REFERENCES wallets(id)
);
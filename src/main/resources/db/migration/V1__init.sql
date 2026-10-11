CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    kyc_status BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    account_type VARCHAR(20) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    balance BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transfers (
    id BIGSERIAL PRIMARY KEY,
    transfer_type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE ledger_entries (
    id BIGSERIAL PRIMARY KEY,
    transfer_id BIGINT NOT NULL REFERENCES transfers(id),
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    direction VARCHAR(10) NOT NULL,
    amount BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT chk_amount
    CHECK(amount > 0)
);
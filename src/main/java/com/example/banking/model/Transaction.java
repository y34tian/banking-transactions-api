package com.example.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID fromAccountId;
    private final UUID toAccountId;
    private final BigDecimal amount;
    private final String currency;
    private final Instant timestamp;
    private final TransactionType type;
    private final String idempotencyKey;

    public Transaction(UUID id, UUID fromAccountId, UUID toAccountId, BigDecimal amount,
                       String currency, Instant timestamp, TransactionType type, String idempotencyKey) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.type = type;
        this.idempotencyKey = idempotencyKey;
    }

    public UUID getId() { return id; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Instant getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
    public String getIdempotencyKey() { return idempotencyKey; }
}

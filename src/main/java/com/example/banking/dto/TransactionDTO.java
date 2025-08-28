package com.example.banking.dto;

import com.example.banking.model.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionDTO {
    private UUID transactionId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    private TransactionType type;

    public TransactionDTO(UUID transactionId, UUID fromAccountId, UUID toAccountId, BigDecimal amount,
                          String currency, Instant timestamp, TransactionType type) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.type = type;
    }

    public UUID getTransactionId() { return transactionId; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Instant getTimestamp() { return timestamp; }
    public TransactionType getType() { return type; }
}

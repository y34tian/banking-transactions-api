package com.example.banking.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransferResponse {
    private UUID transactionId;
    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    private BigDecimal fromBalanceAfter;
    private BigDecimal toBalanceAfter;

    public TransferResponse(UUID transactionId, UUID fromAccountId, UUID toAccountId, BigDecimal amount, String currency,
                            Instant timestamp, BigDecimal fromBalanceAfter, BigDecimal toBalanceAfter) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.fromBalanceAfter = fromBalanceAfter;
        this.toBalanceAfter = toBalanceAfter;
    }

    public UUID getTransactionId() { return transactionId; }
    public UUID getFromAccountId() { return fromAccountId; }
    public UUID getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public Instant getTimestamp() { return timestamp; }
    public BigDecimal getFromBalanceAfter() { return fromBalanceAfter; }
    public BigDecimal getToBalanceAfter() { return toBalanceAfter; }
}

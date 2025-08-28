package com.example.banking.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final String ownerName;
    private BigDecimal balance;
    private final String currency;
    private final Instant createdAt;

    public Account(UUID id, String ownerName, BigDecimal balance, String currency, Instant createdAt) {
        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
}

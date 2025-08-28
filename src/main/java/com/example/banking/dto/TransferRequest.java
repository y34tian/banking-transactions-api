package com.example.banking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequest {
    @NotNull
    private UUID fromAccountId;

    @NotNull
    private UUID toAccountId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String currency = "CAD";

    public UUID getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(UUID fromAccountId) { this.fromAccountId = fromAccountId; }

    public UUID getToAccountId() { return toAccountId; }
    public void setToAccountId(UUID toAccountId) { this.toAccountId = toAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

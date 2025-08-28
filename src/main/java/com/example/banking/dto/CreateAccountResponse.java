package com.example.banking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateAccountResponse {
    private UUID accountId;
    private String ownerName;
    private BigDecimal balance;
    private String currency;

    public CreateAccountResponse(UUID accountId, String ownerName, BigDecimal balance, String currency) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = balance;
        this.currency = currency;
    }

    public UUID getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
}

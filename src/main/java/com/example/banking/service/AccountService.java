package com.example.banking.service;

import com.example.banking.dto.CreateAccountRequest;
import com.example.banking.dto.CreateAccountResponse;
import com.example.banking.dto.TransactionDTO;
import com.example.banking.exception.NotFoundException;
import com.example.banking.model.Account;
import com.example.banking.model.Transaction;
import com.example.banking.repository.InMemoryAccountRepository;
import com.example.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final InMemoryAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(InMemoryAccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public CreateAccountResponse create(CreateAccountRequest req) {
        UUID id = UUID.randomUUID();
        Account account = new Account(id, req.getOwnerName(), req.getInitialBalance().setScale(2), req.getCurrency(), Instant.now());
        accountRepository.save(account);
        return new CreateAccountResponse(account.getId(), account.getOwnerName(), account.getBalance(), account.getCurrency());
    }

    public Account getOrThrow(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new NotFoundException("Account not found: " + id));
    }

    public List<TransactionDTO> history(UUID accountId, int limit) {
        // ensure account exists
        getOrThrow(accountId);
        List<Transaction> txs = transactionRepository.findByAccountId(accountId, limit);
        return txs.stream().map(tx -> new TransactionDTO(
                tx.getId(), tx.getFromAccountId(), tx.getToAccountId(), tx.getAmount(), tx.getCurrency(), tx.getTimestamp(), tx.getType()
        )).collect(Collectors.toList());
    }
}

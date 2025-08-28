package com.example.banking.service;

import com.example.banking.dto.TransferRequest;
import com.example.banking.dto.TransferResponse;
import com.example.banking.exception.BadRequestException;
import com.example.banking.exception.InsufficientFundsException;
import com.example.banking.model.Account;
import com.example.banking.model.Transaction;
import com.example.banking.model.TransactionType;
import com.example.banking.repository.IdempotencyRepository;
import com.example.banking.repository.InMemoryAccountRepository;
import com.example.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionService {

    private final InMemoryAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;

    public TransactionService(InMemoryAccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              IdempotencyRepository idempotencyRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.idempotencyRepository = idempotencyRepository;
    }

    public TransferResponse transfer(TransferRequest req, String idempotencyKey) {
        if (req.getFromAccountId().equals(req.getToAccountId())) {
            throw new BadRequestException("fromAccountId and toAccountId cannot be the same");
        }
        if (req.getAmount().compareTo(new BigDecimal("0.00")) <= 0) {
            throw new BadRequestException("Transfer amount must be positive");
        }
        if (idempotencyKey != null) {
            TransferResponse existed = idempotencyRepository.get(idempotencyKey);
            if (existed != null) return existed;
        }

        Account from = accountRepository.findById(req.getFromAccountId())
                .orElseThrow(() -> new com.example.banking.exception.NotFoundException("From account not found: " + req.getFromAccountId()));
        Account to = accountRepository.findById(req.getToAccountId())
                .orElseThrow(() -> new com.example.banking.exception.NotFoundException("To account not found: " + req.getToAccountId()));

        if (!from.getCurrency().equals(to.getCurrency()) || !from.getCurrency().equals(req.getCurrency())) {
            throw new BadRequestException("Currency mismatch; all must be the same");
        }

        ReentrantLock lockA = accountRepository.lockFor(from.getId());
        ReentrantLock lockB = accountRepository.lockFor(to.getId());

        // Acquire locks in ID order to avoid deadlocks
        Account[] ordered = new Account[]{from, to};
        Arrays.sort(ordered, Comparator.comparing(a -> a.getId().toString()));
        ReentrantLock firstLock = accountRepository.lockFor(ordered[0].getId());
        ReentrantLock secondLock = accountRepository.lockFor(ordered[1].getId());

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (from.getBalance().compareTo(req.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient funds in account " + from.getId());
                }
                BigDecimal amt = req.getAmount().setScale(2);
                from.setBalance(from.getBalance().subtract(amt));
                to.setBalance(to.getBalance().add(amt));
                accountRepository.save(from);
                accountRepository.save(to);

                UUID txId = UUID.randomUUID();
                Instant now = Instant.now();

                // Save two directional entries for clearer history
                Transaction debit = new Transaction(txId, from.getId(), to.getId(), amt, from.getCurrency(), now, TransactionType.TRANSFER_DEBIT, idempotencyKey);
                Transaction credit = new Transaction(txId, from.getId(), to.getId(), amt, to.getCurrency(), now, TransactionType.TRANSFER_CREDIT, idempotencyKey);
                transactionRepository.save(debit);
                transactionRepository.save(credit);

                TransferResponse res = new TransferResponse(txId, from.getId(), to.getId(), amt, from.getCurrency(), now,
                        from.getBalance(), to.getBalance());
                if (idempotencyKey != null) {
                    idempotencyRepository.put(idempotencyKey, res);
                }
                return res;
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}

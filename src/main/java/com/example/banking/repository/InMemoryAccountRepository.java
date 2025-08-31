package com.example.banking.repository;

import com.example.banking.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

// In-memory repository backed by ConcurrentHashMap
@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();
    private final Map<UUID, ReentrantLock> accountLocks = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.getId(), account);
        accountLocks.putIfAbsent(account.getId(), new ReentrantLock());
        return account;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public ReentrantLock lockFor(UUID accountId) {
        accountLocks.putIfAbsent(accountId, new ReentrantLock());
        return accountLocks.get(accountId);
    }
}

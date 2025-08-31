package com.example.banking.repository;

import com.example.banking.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// In-memory repository backed by ConcurrentHashMap
@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();
    private final Map<UUID, List<UUID>> byAccount = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction tx) {
        transactions.put(tx.getId(), tx);
        byAccount.computeIfAbsent(tx.getFromAccountId(), k -> Collections.synchronizedList(new ArrayList<>())).add(tx.getId());
        byAccount.computeIfAbsent(tx.getToAccountId(), k -> Collections.synchronizedList(new ArrayList<>())).add(tx.getId());
        return tx;
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId, int limit) {
        List<UUID> ids = byAccount.getOrDefault(accountId, Collections.emptyList());
        return ids.stream()
                .map(transactions::get)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}

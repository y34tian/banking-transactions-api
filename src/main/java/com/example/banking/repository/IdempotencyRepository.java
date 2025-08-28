package com.example.banking.repository;

import com.example.banking.dto.TransferResponse;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class IdempotencyRepository {
    private final Map<String, TransferResponse> store = new ConcurrentHashMap<>();

    public TransferResponse get(String key) { return store.get(key); }
    public void put(String key, TransferResponse res) { store.put(key, res); }
}

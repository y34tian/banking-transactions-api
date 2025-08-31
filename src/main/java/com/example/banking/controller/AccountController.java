package com.example.banking.controller;

import com.example.banking.dto.CreateAccountRequest;
import com.example.banking.dto.CreateAccountResponse;
import com.example.banking.dto.TransactionDTO;
import com.example.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    /**
     * POST /api/accounts - Create a new account with an initial balance.
     * Validates input via Jakarta Bean Validation and returns the created account summary.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateAccountResponse create(@Valid @RequestBody CreateAccountRequest req) {
        return accountService.create(req);
    }

    /**
     * GET /api/accounts/{accountId}/transactions - Recent transactions for the account (most recent first).
     * @param limit max items (1..200)
     */
    @GetMapping("/{accountId}/transactions")
    public List<TransactionDTO> history(@PathVariable UUID accountId,
                                        @RequestParam(defaultValue = "50") int limit) {
        return accountService.history(accountId, Math.max(1, Math.min(limit, 200)));
    }
}

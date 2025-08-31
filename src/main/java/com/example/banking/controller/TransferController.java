package com.example.banking.controller;

import com.example.banking.dto.TransferRequest;
import com.example.banking.dto.TransferResponse;
import com.example.banking.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/transfers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransferController {

    private final TransactionService transactionService;

    public TransferController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/transfers
     * Transfer funds between two accounts.
     *
     * Request body: {@link TransferRequest}
     *   - fromAccountId (UUID) : source account ID
     *   - toAccountId   (UUID) : destination account ID
     *   - amount        (BigDecimal, >0) : transfer amount
     *   - currency      (String, default "CAD")
     *
     * Response: {@link TransferResponse}
     *   - transactionId
     *   - fromAccountId, toAccountId
     *   - amount, currency, timestamp
     *   - balances after transfer
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public TransferResponse transfer(@Valid @RequestBody TransferRequest req,
                                     @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return transactionService.transfer(req, idempotencyKey);
    }
}

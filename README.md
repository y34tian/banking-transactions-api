# Banking Transactions API (Spring Boot, In‑Memory)

A demo of a banking API with **three‑layer design** (Controller → Service → Repository),
**dependency injection**, **DTOs**, **validation**, **error handling**, **idempotent transfers**, and **in‑memory storage**.

## Features

- Create account with initial balance
- Transfer funds between accounts (atomic, deadlock‑free, idempotency via `Idempotency-Key` header)
- Get transaction history per account (most recent first, with type = DEBIT/CREDIT)
- Input validation with Jakarta Bean Validation
- Global exception handler with consistent JSON errors
- Thread‑safety with per‑account locks and ordered locking
- Minimal tests with MockMvc

## Build & Run

Requirements: Java 17+, Maven 3.9+

```bash
mvn spring-boot:run
```

## API

### Create account

`POST /api/accounts`

```json
{
  "ownerName": "Alice",
  "initialBalance": 100.00,
  "currency": "CAD"
}
```

**200 OK**

```json
{
  "accountId": "uuid",
  "ownerName": "Alice",
  "balance": 100.00,
  "currency": "CAD"
}
```

### Transfer

`POST /api/transfers` (optionally set header `Idempotency-Key: <unique-string>`)

```json
{
  "fromAccountId": "uuid",
  "toAccountId": "uuid",
  "amount": 15.50,
  "currency": "CAD"
}
```

**200 OK**

```json
{
  "transactionId": "uuid",
  "fromAccountId": "uuid",
  "toAccountId": "uuid",
  "amount": 15.50,
  "currency": "CAD",
  "timestamp": "2025-08-26T00:00:00Z",
  "fromBalanceAfter": 84.50,
  "toBalanceAfter": 25.50
}
```

Errors return JSON like:

```json
{ "error": "INSUFFICIENT_FUNDS", "message": "Insufficient funds in account ...", "timestamp": "..." }
```

### History

`GET /api/accounts/{accountId}/transactions?limit=50`

**200 OK**

```json
[
  {
    "transactionId": "uuid",
    "fromAccountId": "uuid",
    "toAccountId": "uuid",
    "amount": 15.50,
    "currency": "CAD",
    "timestamp": "2025-08-26T00:00:00Z",
    "type": "TRANSFER_CREDIT"
  }
]
```

## Assumptions

- Single currency (default CAD). Transfers require matching currency across request and both accounts.
- In‑memory stores only (non‑persistent). Restarting the app loses data.
- Idempotency responses are cached in‑memory keyed by `Idempotency-Key` for demo purposes.
- No authentication included (out of scope).

## Notes

- Lock ordering avoids deadlocks; both balances update atomically for the transfer.
- Monetary values use `BigDecimal` and are kept to 2 decimal places.
- For real systems you’d add auth, audit logs, DB transactions, double‑entry ledger, pagination, etc.

package com.example.banking;

import com.example.banking.dto.CreateAccountRequest;
import com.example.banking.dto.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTransferAndHistory() throws Exception {
        // create two accounts
        CreateAccountRequest a = new CreateAccountRequest();
        a.setOwnerName("Alice");
        a.setInitialBalance(new BigDecimal("100.00"));
        String aRes = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String aId = objectMapper.readTree(aRes).get("accountId").asText();

        CreateAccountRequest b = new CreateAccountRequest();
        b.setOwnerName("Bob");
        b.setInitialBalance(new BigDecimal("10.00"));
        String bRes = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(b)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String bId = objectMapper.readTree(bRes).get("accountId").asText();

        // transfer
        TransferRequest t = new TransferRequest();
        t.setFromAccountId(UUID.fromString(aId));
        t.setToAccountId(UUID.fromString(bId));
        t.setAmount(new BigDecimal("15.50"));
        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk());

        // history
        mockMvc.perform(get("/api/accounts/" + bId + "/transactions"))
                .andExpect(status().isOk());
    }
}

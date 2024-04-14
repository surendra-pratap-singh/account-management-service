package com.ams.controller;

import com.ams.controller.AccountController;
import com.ams.enums.CurrencyType;
import com.ams.model.response.Response;
import com.ams.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testTransferFundsSuccess() throws Exception {
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class)))
                .thenReturn("Transfer successful");

        mockMvc.perform(post("/v1/accounts/transfer")
                .param("sourceAccountId", "123")
                .param("targetAccountId", "456")
                .param("amount", "100")
                .param("currency", "USD")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").value("Transfer successful"));
    }

    @Test
    public void testCreateAccountSuccess() throws Exception {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenReturn("Account created successfully");

        mockMvc.perform(post("/v1/accounts/create")
                .param("clientId", "789")
                .param("currency", "EUR")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data").value("Account created successfully"));
    }

    // More integration tests can be added for other endpoints and scenarios
}

package com.ams.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AccountControllerTest {

    private final MockMvc mockMvc;

    private static final String FUND_TRANSFER_PATH = "/v1/accounts/transfer";
    private static final long SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER = 1712960643757L;
    private static final long TARGET_ACCOUNT_ID_SUCCESSFUL_TRANSFER = 1712960272518L;
    private static final BigDecimal AMOUNT_SUCCESSFUL_TRANSFER = BigDecimal.valueOf(10);
    private static final String ACCOUNT_CREATION_PATH = "/v1/accounts/create";
    private static final long CREATE_ACCOUNT_CLIENT_ID_SUCCESSFUL = 1712957116411L;

    private static final long TARGET_ACCOUNT_ID_FAILED_TRANSFER_NOT_EXISTS = 17129602725189L;

    @Test
    @SneakyThrows
    void testTransferFundsSuccess() {

        mockMvc.perform(post(FUND_TRANSFER_PATH)
                .param("sourceAccountId", String.valueOf(SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER))
                .param("targetAccountId", String.valueOf(TARGET_ACCOUNT_ID_SUCCESSFUL_TRANSFER))
                .param("amount", String.valueOf(AMOUNT_SUCCESSFUL_TRANSFER))
                .param("currency", "INR").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Transfer successful"));
    }

    @Test
    @SneakyThrows
    void testCreateAccountSuccess() {

        mockMvc.perform(post(ACCOUNT_CREATION_PATH)
                .param("clientId", String.valueOf(CREATE_ACCOUNT_CLIENT_ID_SUCCESSFUL))
                .param("currency", "AMD").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.data.currency").value("AMD"))
                .andExpect(jsonPath("$.data.clientId").value(CREATE_ACCOUNT_CLIENT_ID_SUCCESSFUL));

    }

    @Test
    @SneakyThrows
    void testTransferFundsFailed() {

        mockMvc.perform(post(FUND_TRANSFER_PATH)
                .param("sourceAccountId", String.valueOf(SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER))
                .param("targetAccountId", String.valueOf(TARGET_ACCOUNT_ID_FAILED_TRANSFER_NOT_EXISTS))
                .param("amount", String.valueOf(AMOUNT_SUCCESSFUL_TRANSFER))
                .param("currency", "INR").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Target account does not exist"));
    }

}
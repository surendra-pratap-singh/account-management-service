package com.ams.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TransactionControllerTest {

    private final MockMvc mockMvc;
    private static final String TRANSACTIONS_DETAILS_PATH = "/v1/transactions/{accountId}";
    private static final long ACCOUNT_ID_VALID = 1712960643757L;
    private static final long ACCOUNT_ID_INVALID = 171296064370L;

    @Test
    @SneakyThrows
    void testAccountTransactionsDetailsSuccess() {

        mockMvc.perform(get(TRANSACTIONS_DETAILS_PATH, ACCOUNT_ID_VALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.[0].currency").value("EUR"));

    }

    @Test
    @SneakyThrows
    void testAccountTransactionsDetailsFailed() {
        mockMvc.perform(get(TRANSACTIONS_DETAILS_PATH, ACCOUNT_ID_INVALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Transactions not found"));
    }

}
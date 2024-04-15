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
class ClientControllerTest {

    private final MockMvc mockMvc;
    private static final String CLIENT_ACCOUNTS_DETAILS_PATH = "/v1/clients/{clientId}/accounts";
    private static final long CLIENT_ID_VALID = 1712957116411L;
    private static final long CLIENT_ID_INVALID = 171296064379L;

    @Test
    @SneakyThrows
    void testClientAccountsDetailsSuccess() {

        mockMvc.perform(get(CLIENT_ACCOUNTS_DETAILS_PATH, CLIENT_ID_VALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data.[0].accountId").value("1712960643757"))
                .andExpect(jsonPath("$.data.[0].currency").value("EUR"));


    }

    @Test
    @SneakyThrows
    void testClientAccountDetailsFailed() {

        mockMvc.perform(get(CLIENT_ACCOUNTS_DETAILS_PATH, CLIENT_ID_INVALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Record not found for client id 171296064379"));
    }

}
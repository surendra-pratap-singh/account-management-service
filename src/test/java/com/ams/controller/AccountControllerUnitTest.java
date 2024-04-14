package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InternalServerErrorException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.response.Response;
import com.ams.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountControllerUnitTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    public void testTransferFundsSuccess() {
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class)))
                .thenReturn("Transfer successful");

        Response response = accountController.transferFunds(123L, 456L, BigDecimal.valueOf(100), CurrencyType.USD);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Transfer successful", response.getMessage());
    }


    @Test
    public void testCreateAccountSuccessful() {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenReturn("Account created successfully");

        Response response = accountController.createAccount(789L, CurrencyType.EUR);

        assertEquals(HttpStatus.CREATED, response.getStatus());
    }


    @Test
    public void testAccountAlreadyExist() {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenReturn( new Response(HttpStatus.UNPROCESSABLE_ENTITY, "Account already exists for currency:EUR"));

        Response response = accountController.createAccount(789L, CurrencyType.EUR);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatus());
        assertEquals("Account already exists for currency:EUR", response.getMessage());
    }

    // More unit tests can be added for edge cases and error scenarios
}

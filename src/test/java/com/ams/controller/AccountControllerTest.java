package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InternalServerErrorException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.response.Response;
import com.ams.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransferFundsSuccess() {
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class)))
                .thenReturn("Transfer successful");

        Response response = accountController.transferFunds(123L, 456L, BigDecimal.valueOf(100), CurrencyType.USD);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Transfer successful", response.message);
    }

    @Test
    public void testTransferFundsSourceAccountNotFound() {
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class)))
                .thenThrow(new ResourceNotFoundException("Source account not found"));

        Response response = accountController.transferFunds(123L, 456L, BigDecimal.valueOf(100), CurrencyType.USD);

        assertEquals(HttpStatus.NOT_FOUND, response.status);
        assertEquals("Source account not found",response.message);
    }

    @Test
    public void testTransferFunds_InternalServerError() {
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));

        Response response = accountController.transferFunds(123L, 456L, BigDecimal.valueOf(100), CurrencyType.USD);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status);
    }

    @Test
    public void testCreateAccount_SuccessfulCreation() {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenReturn("Account created successfully");

        Response response = accountController.createAccount(789L, CurrencyType.EUR);

        assertEquals(HttpStatus.CREATED, response.status);
        assertEquals("Account created successfully", response.data);
    }

    @Test
    public void testCreateAccountInternalServerError() {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenThrow(new InternalServerErrorException("Internal Server Error"));

        Response response = accountController.createAccount(789L, CurrencyType.USD);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.status);
    }

    @Test
    public void testCreateAccount_ResourceNotFound() {
        when(accountService.createAccount(anyLong(), any(CurrencyType.class)))
                .thenThrow(new ResourceNotFoundException("Resource not found"));

        Response response = accountController.createAccount(789L, CurrencyType.USD);

        assertEquals(HttpStatus.NOT_FOUND, response.status);
    }
}

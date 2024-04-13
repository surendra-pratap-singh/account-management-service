package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InsufficientFundsException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateAccount() {

    }
//
//    @Test
//    void testTransferFunds_SuccessfulTransfer() {
//        // Mocking the service method behavior
//        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class))).thenReturn("Funds transferred successfully");
//
//        // Performing the test
//        ResponseEntity<String> response = accountController.transferFunds(1L, 2L, BigDecimal.TEN, CurrencyType.EUR);
//
//        // Verifying the behavior
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Funds transferred successfully", response.getBody());
//        verify(accountService, times(1)).transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class));
//    }

    @Test
    void testTransferFunds_SourceAccountNotFound() {
        // Mocking the service method behavior to throw ResourceNotFoundException
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class))).thenThrow(new ResourceNotFoundException("Source account not found"));

        // Performing the test
        assertThrows(ResourceNotFoundException.class, () -> accountController.transferFunds(1L, 2L, BigDecimal.TEN, CurrencyType.EUR));
    }

    @Test
    void testTransferFunds_InsufficientFunds() {
        // Mocking the service method behavior to throw InsufficientFundsException
        when(accountService.transferFunds(anyLong(), anyLong(), any(BigDecimal.class), any(CurrencyType.class))).thenThrow(new InsufficientFundsException("Insufficient funds"));

        // Performing the test
        assertThrows(InsufficientFundsException.class, () -> accountController.transferFunds(1L, 2L, BigDecimal.valueOf(100), CurrencyType.EUR));
    }

}
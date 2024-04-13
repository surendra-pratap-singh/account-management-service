package com.ams.service;


import com.ams.enums.CurrencyType;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.db.Transaction;
import com.ams.model.dto.TransactionDto;
import com.ams.repository.AccountRepository;
import com.ams.repository.ClientRepository;
import com.ams.repository.TransactionRepository;
import com.ams.util.CurrencyExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CurrencyExchange currencyExchange;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTransactionsSuccess() {
        Long accountId = 1L;
        int offset = 0;
        int limit = 10;
        Transaction tansaction1 = Transaction.builder()
                .transactionId(1713017962413L)
                .currency("EUR")
                .amount(BigDecimal.valueOf(10.00))

                .build();
        Transaction tansaction2 = Transaction.builder().build();

        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionRepository.findByAccountIdOrderByDateDesc(accountId, PageRequest.of(offset, limit))).thenReturn(transactions);

        List<TransactionDto> result = accountService.getTransactions(accountId, offset, limit);

        assertEquals(transactions.size(), result.size());
        verify(transactionRepository).findByAccountIdOrderByDateDesc(accountId, PageRequest.of(offset, limit));
    }


    // Write similar tests for other scenarios like insufficient funds, invalid currency, etc.

//    @Test
//    void creatAccount_SuccessfulCreation() {
//        // Given
//        Long clientId = 1L;
//        CurrencyType currency = CurrencyType.USD;
//        Client client = new Client();
//        client.setId(clientId);
//        when(clientRepository.findClientByClientId(clientId)).thenReturn(client);
//        when(accountRepository.findByClientAndCurrency(client, currency.name())).thenReturn(Collections.emptyList());
//        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // When
//        AccountDto result = accountService.creatAccount(clientId, currency);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(clientId, result.getClientId());
//        assertEquals(currency.toString(), result.getCurrency());
//        assertEquals(0, result.getBalance().compareTo(BigDecimal.ZERO));
//    }

//    @Test
//    void creatAccount_AccountExistsForCurrency_ThrowsInvalidRequestException() {
//        // Given
//        Long clientId = 1L;
//        CurrencyType currency = CurrencyType.USD;
//        Client client = new Client();
//        client.setId(clientId);
//        when(clientRepository.findClientByClientId(clientId)).thenReturn(client.get);
//        when(accountRepository.findByClientAndCurrency(client, currency.name())).thenReturn(List.of(new Account()));
//
//        // When / Then
//        assertThrows(InvalidRequestException.class, () -> accountService.creatAccount(clientId, currency));
//    }

    @Test
    void creatAccount_ClientNotFound_ThrowsResourceNotFoundException() {
        // Given
        Long clientId = 1L;
        CurrencyType currency = CurrencyType.USD;
        when(clientRepository.findClientByClientId(clientId)).thenReturn(null);

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> accountService.creatAccount(clientId, currency));
    }
}
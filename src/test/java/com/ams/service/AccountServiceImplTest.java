package com.ams.service;

import com.ams.enums.CurrencyType;
import com.ams.enums.TransactionType;
import com.ams.exception.InsufficientFundsException;
import com.ams.exception.InvalidCurrencyException;
import com.ams.model.db.Account;
import com.ams.model.db.Client;
import com.ams.model.db.Transaction;
import com.ams.model.dto.TransactionDto;
import com.ams.repository.AccountRepository;
import com.ams.repository.ClientRepository;
import com.ams.repository.TransactionRepository;
import com.ams.util.AmsUtils;
import com.ams.util.CurrencyExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private CurrencyExchange currencyExchange;

    @InjectMocks
    private AccountServiceImpl accountService;



    private static final long CLIENT_ID_1 = 1712957116411L;
    private static final long CLIENT_ID_2 = 1712957116412L;
    private static final long CLIENT_ID_3 = 1712957116413L;

    private static final long SOURCE_ACCOUNT_ID_1 = 1712960643757L;
    private static final long SOURCE_ACCOUNT_ID_2 = 1712960651227L;
    private static final long TARGET_ACCOUNT_ID_1 = 1713960272588L;
    private static final long TARGET_ACCOUNT_ID_2 = 1712960272518L;

    private static final long SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER = 1712960643757L;
    private static final long TARGET_ACCOUNT_ID_SUCCESSFUL_TRANSFER = 1712960272518L;
    private static final BigDecimal AMOUNT_SUCCESSFUL_TRANSFER = BigDecimal.valueOf(10);
    private static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(10);
    private static final long CREATE_ACCOUNT_CLIENT_ID_SUCCESSFUL = 1712957116411L;
    private static final long TARGET_ACCOUNT_ID_FAILED_TRANSFER_NOT_EXISTS = 17129602725189L;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        clientRepository = mock(ClientRepository.class);
        currencyExchange = mock(CurrencyExchange.class);
        accountService = new AccountServiceImpl(transactionRepository, accountRepository, clientRepository, currencyExchange);
    }

    private List<Client> getClients() {
        Client client1 = Client.builder().clientId(CLIENT_ID_1).email("john.doe@example.com").firstName("John").lastName("Doe").build();
        Client client2 = Client.builder().clientId(CLIENT_ID_2).email("jane.smith@example.com").firstName("Jane").lastName("Smith").build();
        return Arrays.asList(client1, client2);
    }

    private List<Account> getAccounts() {
        Account account1 = Account.builder().currency("EUR").client(getClients().get(0)).balance(BigDecimal.valueOf(100)).accountId(SOURCE_ACCOUNT_ID_1).build();
        Account account2 = Account.builder().currency("USD").client(getClients().get(1)).balance(BigDecimal.valueOf(100)).accountId(SOURCE_ACCOUNT_ID_2).build();
        Account account3 = Account.builder().currency("EUR").client(getClients().get(1)).balance(BigDecimal.valueOf(100)).accountId(TARGET_ACCOUNT_ID_1).build();
        Account account4 = Account.builder().currency("INR").client(getClients().get(0)).balance(BigDecimal.valueOf(100)).accountId(TARGET_ACCOUNT_ID_2).build();
        return Arrays.asList(account1, account2, account3, account4);
    }

    private List<Transaction> getTransactions() {

        Transaction transaction1 = Transaction.builder().amount(BigDecimal.valueOf(5)).date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId()).currency("EUR")
                .type(TransactionType.DEBIT.name()).account(getAccounts().get(0)).build();

        Transaction transaction2 = Transaction.builder().amount(BigDecimal.valueOf(5)).date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId()).currency("EUR")
                .type(TransactionType.CREDIT.name()).account(getAccounts().get(2)).build();

        Transaction transaction3 = Transaction.builder().amount(BigDecimal.valueOf(5.50)).date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId()).currency("USD")
                .type(TransactionType.DEBIT.name()).account(getAccounts().get(1)).build();

        Transaction transaction4 = Transaction.builder().amount(BigDecimal.valueOf(459.24)).date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId()).currency("INR")
                .type(TransactionType.CREDIT.name()).account(getAccounts().get(3)).build();

        return Arrays.asList(transaction1, transaction2, transaction3, transaction4);
    }

    @Test
    void testGetTransactions() {
        Long accountId = 123L;
        int offset = 0;
        int limit = 10;

        when(transactionRepository.findByAccountIdOrderByDateDesc(accountId, PageRequest.of(offset, limit)))
                .thenReturn(getTransactions());

        List<TransactionDto> transactions = accountService.getTransactions(accountId, offset, limit);

        assertEquals(4, transactions.size());
    }

    @Test
    void testTransferFundsWithValidData() {
        Long sourceAccountId = 1L;
        Long targetAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(100);
        CurrencyType currency = CurrencyType.EUR;

        when(accountRepository.findByAccountId(anyLong()))
                .thenReturn(Optional.of(getAccounts().get(0)));
        when(accountRepository.findByAccountId(anyLong()))
                .thenReturn(Optional.of(getAccounts().get(1)));

        String result = accountService.transferFunds(sourceAccountId, targetAccountId, amount, currency);

        assertEquals("Transfer successful", result);
    }

    @Test
    void testTransferFundsWithInvalidCurrency() {
        BigDecimal amount = BigDecimal.valueOf(10);
        CurrencyType currency = CurrencyType.USD;

        when(accountRepository.findByAccountId(anyLong()))
                .thenReturn(Optional.of(getAccounts().get(0)));
        when(accountRepository.findByAccountId(anyLong()))
                .thenReturn(Optional.of(getAccounts().get(1)));
        InvalidCurrencyException thrown = assertThrows(
                InvalidCurrencyException.class,
                () -> accountService.transferFunds(SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER, TARGET_ACCOUNT_ID_FAILED_TRANSFER_NOT_EXISTS, amount, currency),
                "Invalid currency"
        );
        assertTrue(thrown.getMessage().contains("Invalid currency"));
    }

    @Test
    void testTransferFundsWithInsufficientFunds() {
        BigDecimal amount = BigDecimal.valueOf(10000);
        CurrencyType currency = CurrencyType.EUR;

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(getAccounts().get(0)));

        when(accountRepository.findByAccountId(anyLong())).thenReturn(Optional.of(getAccounts().get(1)));

        InsufficientFundsException thrown = assertThrows(
                InsufficientFundsException.class,
                () -> accountService.transferFunds(SOURCE_ACCOUNT_ID_SUCCESSFUL_TRANSFER, TARGET_ACCOUNT_ID_FAILED_TRANSFER_NOT_EXISTS, amount, currency),
                "Insufficient funds"
        );
        assertTrue(thrown.getMessage().contains("Insufficient funds"));
    }

    // Write more test cases for other methods...
}

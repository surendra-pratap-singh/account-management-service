package com.ams.service;

import com.ams.enums.CurrencyType;
import com.ams.enums.TransactionType;
import com.ams.exception.InsufficientFundsException;
import com.ams.exception.InvalidCurrencyException;
import com.ams.exception.InvalidRequestException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.mapper.AccountMapper;
import com.ams.mapper.TransactionMapper;
import com.ams.model.db.Account;
import com.ams.model.db.Client;
import com.ams.model.db.Transaction;
import com.ams.model.dto.AccountDto;
import com.ams.model.dto.TransactionDto;
import com.ams.repository.AccountRepository;
import com.ams.repository.ClientRepository;
import com.ams.repository.TransactionRepository;
import com.ams.util.CurrencyExchange;
import com.ams.util.IdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CurrencyExchange exchange;

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class.getSimpleName());

    public List<TransactionDto> getTransactions(Long accountId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<Transaction> transaction = transactionRepository.findByAccountIdOrderByDateDesc(accountId, pageable);
        return transaction.stream()
                .map(TransactionMapper::mapTransactionEntityToDto)
                .toList();
    }

    @Transactional
    public String transferFunds(Long sourceAccountId, Long targetAccountId, BigDecimal amount, CurrencyType currency) {

        Account sourceAccount = accountRepository.findByAccountId(sourceAccountId);
        if (sourceAccount.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Account targetAccount = accountRepository.findByAccountId(targetAccountId);

        log.info("Condition 1: {} ",sourceAccount.getBalance().compareTo(amount));
        log.info("Condition 2: {} ",sourceAccount.getBalance().compareTo(amount) < 0);
        log.info("sourceAccount.getBalance() : {} ",sourceAccount.getBalance());
        log.info("amount : {} ",amount);
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            log.info("sourceAccount.getBalance() : {} ",sourceAccount.getBalance());
            throw new InsufficientFundsException("Insufficient funds");
        }
        log.info("Condition 3: {} ",!targetAccount.getCurrency().equalsIgnoreCase(currency.name()));
        if (!targetAccount.getCurrency().equalsIgnoreCase(currency.name())) {
            log.info("targetAccount.getCurrency() : {} ",targetAccount.getCurrency());
            throw new InvalidCurrencyException("Select the correct currency");
        }
        if (sourceAccount.getCurrency().equalsIgnoreCase(targetAccount.getCurrency())) {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            targetAccount.setBalance(targetAccount.getBalance().add(amount));
            saveTransaction(sourceAccount, targetAccount, amount, amount);
        } else {
            BigDecimal exchangeAmount = exchange.getExchangedCurrency(sourceAccount.getCurrency(), targetAccount.getCurrency(), amount);
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            targetAccount.setBalance(targetAccount.getBalance().add(exchangeAmount));
            saveTransaction(sourceAccount, targetAccount, amount, exchangeAmount);
        }
        accountRepository.saveAll(List.of(sourceAccount, targetAccount));
        return "Funds transferred";


    }

    private void saveTransaction(Account sourceAccount, Account targetAccount, BigDecimal sourceAmount, BigDecimal targetAmount) {

        transactionRepository.save(Transaction.builder()
                .amount(sourceAmount)
                .date(new Date())
                .transactionId(IdGenerator.generate())
                .currency(sourceAccount.getCurrency())
                .type(TransactionType.DEBIT.name())
                .account(sourceAccount)
                .build());

        transactionRepository.save(Transaction.builder()
                .amount(targetAmount)
                .date(new Date())
                .transactionId(IdGenerator.generate())
                .currency(targetAccount.getCurrency())
                .type(TransactionType.CREDIT.name())
                .account(targetAccount)
                .build());

    }

    @Override
    public AccountDto creatAccount(Long clientId, CurrencyType currency) {
        Client client = clientRepository.findClientByClientId(clientId);
        if (client != null) {
            boolean flag = false;
            List<Account> existingAccounts = accountRepository.findByClientAndCurrency(client, currency.name());
            if (existingAccounts != null && !ObjectUtils.isEmpty(existingAccounts)) {
                flag = existingAccounts.stream()
                        .anyMatch(account -> currency.name()
                                .equalsIgnoreCase(account.getCurrency()));
            }
            if (!flag) {
                Account account = Account.builder().currency(currency.toString())
                        .client(client)
                        .balance(BigDecimal.ZERO)
                        .accountId(IdGenerator.generate())
                        .build();
                return AccountMapper.mapEntityToDto(accountRepository.save(account));
            } else {
                throw new InvalidRequestException("Account already exists for currency: " + currency);
            }
        } else {
            throw new ResourceNotFoundException("No record found for Client Id" + clientId);
        }
    }
}
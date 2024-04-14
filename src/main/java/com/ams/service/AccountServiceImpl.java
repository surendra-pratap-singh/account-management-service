package com.ams.service;

import com.ams.enums.CurrencyType;
import com.ams.enums.TransactionType;
import com.ams.exception.*;
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
import com.ams.util.AmsUtils;
import com.ams.util.CurrencyExchange;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CurrencyExchange exchange;

    public List<TransactionDto> getTransactions(Long accountId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<Transaction> transaction = transactionRepository.findByAccountIdOrderByDateDesc(accountId, pageable);
        log.trace("List of transaction from db {}",transaction.size());
        return transaction.stream()
                .map(TransactionMapper::mapTransactionEntityToDto)
                .toList();
    }

    @Transactional
    public String transferFunds(Long sourceAccountId, Long targetAccountId, BigDecimal amount, CurrencyType currency) {

        Optional<Account> sourceAccount = accountRepository.findByAccountId(sourceAccountId);
        if(sourceAccount.isEmpty()){
            log.error("Source account does not exist {}",AmsUtils.maskData(sourceAccountId));
            throw new InvalidRequestException("Source account does not exist");
        }

        if (sourceAccount.get().getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Insufficient funds");
            throw new InsufficientFundsException("Insufficient funds");
        }

        Optional<Account> targetAccount = accountRepository.findByAccountId(targetAccountId);
        if(targetAccount.isEmpty()){
            log.error("Target account does not exist {}",AmsUtils.maskData(sourceAccountId));
            throw new InvalidRequestException("Target account does not exist");
        }

        if (sourceAccount.get().getBalance().compareTo(amount) < 0) {
            log.error("Insufficient funds");
            throw new InsufficientFundsException("Insufficient funds");
        }
         if (!targetAccount.get().getCurrency().equalsIgnoreCase(currency.name())) {
            log.info("Invalid currency {} ",targetAccount.get().getCurrency());
            throw new InvalidCurrencyException("Invalid currency");
        }
        if (sourceAccount.get().getCurrency().equalsIgnoreCase(targetAccount.get().getCurrency())) {
            sourceAccount.get().setBalance(sourceAccount.get().getBalance().subtract(amount));
            targetAccount.get().setBalance(targetAccount.get().getBalance().add(amount));
            saveTransaction(sourceAccount.get(), targetAccount.get(), amount, amount);
        } else {
            BigDecimal exchangeAmount = exchange.getExchangedCurrency(sourceAccount.get().getCurrency(), targetAccount.get().getCurrency(), amount);
            sourceAccount.get().setBalance(sourceAccount.get().getBalance().subtract(amount));
            targetAccount.get().setBalance(targetAccount.get().getBalance().add(exchangeAmount));
            saveTransaction(sourceAccount.get(), targetAccount.get(), amount, exchangeAmount);
        }
        accountRepository.saveAll(List.of(sourceAccount.get(), targetAccount.get()));
        return "Funds transferred";
    }

    private void saveTransaction(Account sourceAccount, Account targetAccount, BigDecimal sourceAmount, BigDecimal targetAmount) {

    try {
        Transaction debitTransaction = Transaction.builder()
                .amount(sourceAmount)
                .date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId())
                .currency(sourceAccount.getCurrency())
                .type(TransactionType.DEBIT.name())
                .account(sourceAccount)
                .build();

        transactionRepository.save(debitTransaction);
        log.trace("Saving DEBIT transaction");

        Transaction creditTransaction =  Transaction.builder()
                .amount(targetAmount)
                .date(LocalDateTime.now())
                .transactionId(AmsUtils.generateUniqueId())
                .currency(targetAccount.getCurrency())
                .type(TransactionType.CREDIT.name())
                .account(targetAccount)
                .build();
        transactionRepository.save(creditTransaction);
        log.trace("Saving CREDIT transaction");
    }catch (Exception e){
        log.trace(e.getMessage());
        throw new InternalServerErrorException("Error while saving the object");
    }
    }

    @Override
    public AccountDto creatAccount(Long clientId, CurrencyType currency) {
        Optional<Client> client = clientRepository.findClientByClientId(clientId);
        if(client.isPresent()){
            boolean flag = false;
            List<Account> existingAccounts = accountRepository.findByClientAndCurrency(client.get(), currency.name());
            if (existingAccounts != null && !ObjectUtils.isEmpty(existingAccounts)) {
                flag = existingAccounts.stream()
                        .anyMatch(account -> currency.name()
                                .equalsIgnoreCase(account.getCurrency()));
            }
            if (!flag) {
                Account account = Account.builder().currency(currency.toString())
                        .client(client.get())
                        .balance(BigDecimal.ZERO)
                        .accountId(AmsUtils.generateUniqueId())
                        .build();
                return AccountMapper.mapEntityToDto(accountRepository.save(account));
            } else {
                log.error("Account already exists for currency {}",currency);
                throw new InvalidRequestException("Account already exists for currency: " + currency);
            }
        } else {
            log.error("Account already exists for currency {}",currency);
            throw new ResourceNotFoundException("No record found for Client Id" + clientId);
        }
    }
}
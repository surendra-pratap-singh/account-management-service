package com.ams.service;

import com.ams.enums.CurrencyType;
import com.ams.model.dto.AccountDto;
import com.ams.model.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;


public interface AccountService {

    List<TransactionDto> getTransactions(Long accountId, int offset, int limit);

    String transferFunds(Long sourceAccountId, Long targetAccountId, BigDecimal amount, CurrencyType currency) ;

    Object createAccount(Long clientId, CurrencyType currency);

}
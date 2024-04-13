package com.ams.mapper;

import com.ams.model.db.Account;
import com.ams.model.dto.AccountDto;

public class AccountMapper {
    public static AccountDto mapEntityToDto(Account account){
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .clientId(account.getClient().getClientId())
                .build();
    }

}

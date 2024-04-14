package com.ams.service;

import com.ams.model.dto.AccountDto;

import java.util.List;


public interface ClientService {

    List<AccountDto> getClientAccounts(Long clientId);

}
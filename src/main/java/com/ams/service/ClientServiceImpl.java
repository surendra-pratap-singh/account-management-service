package com.ams.service;

import com.ams.exception.ResourceNotFoundException;
import com.ams.mapper.AccountMapper;
import com.ams.model.db.Account;
import com.ams.model.db.Client;
import com.ams.model.dto.AccountDto;
import com.ams.repository.AccountRepository;
import com.ams.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<AccountDto> getClientAccounts(Long clientId) {
        Client client = clientRepository.findClientByClientId(clientId);
        if(client != null){
            List<Account> accountList = accountRepository.findByClient(client);
            return accountList.stream().
                    map(AccountMapper::mapEntityToDto).collect(Collectors.toList());
        }
        else {
            throw new ResourceNotFoundException("Record not found for client id "+clientId);
        }
    }

}
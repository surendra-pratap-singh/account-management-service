package com.ams.service;

import com.ams.exception.ResourceNotFoundException;
import com.ams.mapper.AccountMapper;
import com.ams.model.db.Account;
import com.ams.model.db.Client;
import com.ams.model.dto.AccountDto;
import com.ams.repository.AccountRepository;
import com.ams.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    public List<AccountDto> getClientAccounts(Long clientId) {
        Optional <Client> client = clientRepository.findClientByClientId(clientId);
        if(client.isPresent()){
            log.trace("Client found with clientId {}",clientId);
            List<Account> accountList = accountRepository.findByClient(client.get());
            if(accountList != null && !accountList.isEmpty()) {
                return accountList.stream().
                        map(AccountMapper::mapEntityToDto).collect(Collectors.toList());
            }
            else {
                log.error("Record not found");
                throw new ResourceNotFoundException("Record not found");
            }
        }
        else {
            log.error("Record not found");
            throw new ResourceNotFoundException("Record not found");
        }
    }

}
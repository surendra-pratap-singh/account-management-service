package com.ams.repository;

import com.ams.model.db.Account;
import com.ams.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountId(Long accountId);

    List<Account> findByClientAndCurrency(Client client, String currency);


    List<Account> findByClient(Client client);
}

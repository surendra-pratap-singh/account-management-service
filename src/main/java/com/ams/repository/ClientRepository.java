package com.ams.repository;

import com.ams.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByClientId(Long clientId);

    Client findByClientId(Long clientId);

}

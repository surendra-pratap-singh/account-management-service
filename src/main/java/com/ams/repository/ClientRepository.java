package com.ams.repository;

import com.ams.model.db.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByClientId(Long clientId);

    Optional<Client> findByClientId(Long clientId);

}

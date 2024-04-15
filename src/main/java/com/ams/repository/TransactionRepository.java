package com.ams.repository;

import com.ams.model.db.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.account a WHERE a.accountId = ?1 ORDER BY t.date DESC")
    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId, Pageable pageable);

}

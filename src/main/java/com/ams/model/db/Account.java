package com.ams.model.db;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account_tbl", indexes = {
    @Index(name = "client_id_index", columnList = "client_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "account_balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "account_currency", nullable = false)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @JsonBackReference
    private Client client;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private List<Transaction> transactions;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(accountId, account.accountId) && Objects.equals(balance, account.balance) && Objects.equals(currency, account.currency) && Objects.equals(client, account.client) && Objects.equals(transactions, account.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, balance, currency, client, transactions);
    }
}
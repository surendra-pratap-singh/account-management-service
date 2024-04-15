package com.ams.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "transaction_tbl")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transaction {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "txn_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "txn_currency", nullable = false)
    private String currency;

    @Column(name = "txn_type", nullable = false)
    private String type;

    @Column(name = "txn_date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) && Objects.equals(transactionId, that.transactionId) && Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency) && Objects.equals(type, that.type) && Objects.equals(date, that.date) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionId, amount, currency, type, date, account);
    }
}
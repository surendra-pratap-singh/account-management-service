package com.ams.mapper;

import com.ams.model.db.Transaction;
import com.ams.model.dto.TransactionDto;

public class TransactionMapper {
    public static TransactionDto mapTransactionEntityToDto(Transaction transaction){
        return TransactionDto.builder()
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .date(transaction.getDate())
                .type(transaction.getType())
                .transactionId(transaction.getTransactionId())
                .build();
    }

}

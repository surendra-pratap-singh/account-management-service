package com.ams.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @Schema(description = "This is the transaction amount", example = "0.0")
    private BigDecimal amount;

    @Schema(description = "This is the transaction id",  example = "7029eba7-bfd1-48ca-a436-e745b9868fd1")
    private Long transactionId;

    @Schema(description = "This is the transaction currency", example = "EUR")
    private String currency;

    @Schema(description = "This is the transaction type", example = "DEBIT")
    private String type;

    @Schema(description = "This is the transaction date", example = "yyyy-MM-dd")
    private LocalDateTime date;

}
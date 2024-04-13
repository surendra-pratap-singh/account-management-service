package com.ams.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @Schema(description = "Unique ID for account", example = "7029eba7-bfd1-48ca-a436-e745b9868fd1")
    private Long accountId;

    @Schema(description = "Balance in the bank account", example = "0.0")
    private BigDecimal balance;

    @Schema(description = "Bank account currency", example = "EUR")
    private String currency;

    @Schema(description = "ClientId of account holder", example = "7029eba7-bfd1-48ca-a436-e745b9868fd1")
    private Long clientId;
}
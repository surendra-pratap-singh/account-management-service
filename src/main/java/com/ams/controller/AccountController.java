package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InsufficientFundsException;
import com.ams.exception.InternalServerErrorException;
import com.ams.exception.InvalidCurrencyException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.dto.AccountDto;
import com.ams.model.response.Response;
import com.ams.service.AccountService;
import com.ams.util.AmsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Tag(name = "Account API", description = "Account management service")
@RequestMapping(value = "/v1/accounts", produces = "application/json")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @Operation(description = "Funds transfer from source account to target account by accountId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funds transferred successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "422", description = "Insufficient funds", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InsufficientFundsException.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "422", description = "Invalid currency", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidCurrencyException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @PostMapping("/transfer")
    public Response transferFunds(@RequestParam @NonNull Long sourceAccountId,
                                                  @RequestParam @NonNull Long targetAccountId,
                                                  @RequestParam @NonNull BigDecimal amount,
                                                  @RequestParam @NonNull CurrencyType currency) {

        log.debug("Request received to transfer funds from account {}", AmsUtils.maskData(sourceAccountId));
        return new Response(HttpStatus.OK, accountService.transferFunds(sourceAccountId, targetAccountId, amount, currency));
    }

    @Operation(description = "This API is to create new currency account.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Account created successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @PostMapping("/create")
    public Response createAccount(@RequestParam @NonNull Long clientId,
                                                    @RequestParam @NonNull CurrencyType currency) {
        log.debug("Request received to create new account for client {} currency {}", AmsUtils.maskData(clientId),currency.name());
        return new Response(HttpStatus.CREATED,accountService.createAccount(clientId, currency));
    }
}
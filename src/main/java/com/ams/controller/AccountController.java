package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InternalServerErrorException;
import com.ams.exception.InvalidRequestException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.dto.AccountDto;
import com.ams.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Tag(name = "Account API", description = "Account management service")
@RequestMapping(value = "/v1/accounts", produces = "application/json")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(description = "Funds transfer from source account to target account by accountId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Funds transferred successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestParam Long sourceAccountId,
                                                @RequestParam Long targetAccountId,
                                                @RequestParam BigDecimal amount,
                                                @RequestParam CurrencyType currency) {
        // on success it will return 204
        var response = accountService.transferFunds(sourceAccountId, targetAccountId, amount,currency);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "This API is to create new currency account.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account created successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestParam Long clientId,
                                                    @RequestParam CurrencyType currency) {
        //validate request params here
        if (clientId != null && currency != null) {
            return new ResponseEntity<>(accountService.creatAccount(clientId, currency), HttpStatus.OK);
        }
        else
            throw new InvalidRequestException("Enter valid clientId or currency");

    }
}
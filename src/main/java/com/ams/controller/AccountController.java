package com.ams.controller;

import com.ams.enums.CurrencyType;
import com.ams.exception.InternalServerErrorException;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    public ResponseEntity<Response> transferFunds(@RequestParam @NonNull Long sourceAccountId,
                                                  @RequestParam @NonNull Long targetAccountId,
                                                  @RequestParam @NonNull BigDecimal amount,
                                                  @RequestParam @NonNull CurrencyType currency) {

        log.debug("Request received to transfer funds from account {}", AmsUtils.maskData(sourceAccountId));
        Response response = new Response(HttpStatus.NO_CONTENT, accountService.transferFunds(sourceAccountId, targetAccountId, amount, currency));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
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
    public ResponseEntity<Response> createAccount(@RequestParam @NonNull Long clientId,
                                                    @RequestParam @NonNull CurrencyType currency) {
        log.debug("Request received to create new account for client {} currency {}", AmsUtils.maskData(clientId),currency.name());
        Response response = new Response(HttpStatus.CREATED,accountService.creatAccount(clientId, currency));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
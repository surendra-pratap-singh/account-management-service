package com.ams.controller;

import com.ams.exception.InternalServerErrorException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.dto.TransactionDto;
import com.ams.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/transactions", produces = "application/json")
@Tag(name = "Transaction API", description = "Transaction management service")
@RequiredArgsConstructor
public class TransactionController {

    private final AccountService accountService;

    @Operation(description = "Get account transactions by accountId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction details retrieved successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionDto>> getAccountTransactions(@PathVariable Long accountId,
                                                       @RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "10") int limit) {
        log.info("Getting transactions for accountId:{}",accountId);
        return new ResponseEntity<>(accountService.getTransactions(accountId, offset, limit), HttpStatus.OK);
    }

}
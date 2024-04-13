package com.ams.controller;

import com.ams.exception.InternalServerErrorException;
import com.ams.exception.InvalidRequestException;
import com.ams.exception.ResourceNotFoundException;
import com.ams.model.dto.AccountDto;
import com.ams.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/clients", produces = "application/json")
@Tag(name = "Client API", description = "Client management service")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(description = "Get account list by clientId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account details retrieved successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Not Found Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceNotFoundException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorException.class))
                    })
            })
    @GetMapping("/{clientId}/accounts")
    public ResponseEntity<List<AccountDto>> getClientAccounts(@PathVariable Long clientId) {
        log.debug("Received request to get client accounts for clientId : {}", clientId);
        if(clientId != null){
            return new ResponseEntity<>(clientService.getClientAccounts(clientId), HttpStatus.OK);
        }
        else{
            throw new InvalidRequestException("Invalid clientId");
        }

    }


}
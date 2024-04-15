package com.ams.exception;

import com.ams.model.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void testInsufficientFundsException() {
        String errorMessage = "Insufficient funds";
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) errorHandler.insufficientFundsException(new InsufficientFundsException(errorMessage));

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals(errorMessage, ((Response) responseEntity.getBody()).getMessage());
    }

    @Test
    void testInternalServerErrorException() {
        String errorMessage = "Internal server error";
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) errorHandler.internalServerErrorException(new InternalServerErrorException(errorMessage));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(errorMessage, ((Response) responseEntity.getBody()).getMessage());
    }

    @Test
    void testInvalidCurrencyException() {
        String errorMessage = "Invalid currency";
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) errorHandler.invalidCurrencyException(new InvalidCurrencyException(errorMessage));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals(errorMessage, ((Response) responseEntity.getBody()).getMessage());
    }

    @Test
    void testInvalidRequestException() {
        String errorMessage = "Invalid request";
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) errorHandler.returnInvalidRequest(new InvalidRequestException(errorMessage));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errorMessage, ((Response) responseEntity.getBody()).getMessage());
    }

    @Test
    void testResourceNotFoundException() {
        String errorMessage = "Resource not found";
        ResponseEntity<Object> responseEntity = (ResponseEntity<Object>) errorHandler.resourceNotFoundException(new ResourceNotFoundException(errorMessage));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, ((Response) responseEntity.getBody()).getMessage());
    }
}


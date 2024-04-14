package com.ams.exception;

import com.ams.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseBody
    private Object insufficientFundsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new Response(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseBody
    private Object internalServerErrorException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Response(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    @ResponseBody
    private Object invalidCurrencyException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new Response(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseBody
    private Object returnInvalidRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    private Object resourceNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Response(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList()));
    }

}

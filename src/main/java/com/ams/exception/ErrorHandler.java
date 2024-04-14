package com.ams.exception;

import com.ams.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Optional;

@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(InsufficientFundsException.class)
	@ResponseBody
	private Object insufficientFundsException(Exception ex) {
		return new Response(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
	}

	@ExceptionHandler(InternalServerErrorException.class)
	@ResponseBody
	private Object internalServerErrorException(Exception ex) {
		return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(InvalidCurrencyException.class)
	@ResponseBody
	private Object invalidCurrencyException(Exception ex) {
		return new Response(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
	}

	@ExceptionHandler(InvalidRequestException.class)
	@ResponseBody
	private Object returnInvalidRequest(Exception ex) {
		return new Response(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	private Object resourceNotFoundException(Exception ex) {
		return new Response(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList());
	}
	
}

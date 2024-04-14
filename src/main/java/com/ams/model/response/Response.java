package com.ams.model.response;

import org.springframework.http.HttpStatus;

public class Response {

    public HttpStatus status;
    public String message = "Success";
    public Object data;

    public Response(HttpStatus httpStatus, String message, Object data) {
        this.message = message;
        this.status = httpStatus;
        this.data = data;
    }

    public Response(HttpStatus httpStatus, String message) {
        this.message = message;
        this.status = httpStatus;
    }

    public Response(HttpStatus httpStatus, Object data) {
        this.data = data;
        this.status = httpStatus;
    }
}

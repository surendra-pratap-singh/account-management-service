package com.ams.model.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class Response {

    private HttpStatus status;
    private String message = "Success";
    private  Object data;

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

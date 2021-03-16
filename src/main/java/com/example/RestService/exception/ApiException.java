package com.example.RestService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    @Getter
    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
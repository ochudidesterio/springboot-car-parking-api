package com.example.RestService.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@AllArgsConstructor
public class ApiErrorResponse {

    @Getter
    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final Instant timestamp;


}

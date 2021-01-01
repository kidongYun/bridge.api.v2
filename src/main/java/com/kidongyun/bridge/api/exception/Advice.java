package com.kidongyun.bridge.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

@RestControllerAdvice
public class Advice {
    /** Common Exception Handler */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /** Custom Exception Handler */
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<?> httpStatusCodeException(HttpStatusCodeException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getStatusCode().getReasonPhrase());
    }
}

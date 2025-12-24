package com.toolborrow.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TBAException.class)
    public ResponseEntity<String> handleApiException(TBAException ex) {

        log.warn("ApiException occurred: status={}, message={}", ex.getStatus(), ex.getMessage());

        return ResponseEntity
            .status(ex.getStatus())
            .body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {

        log.error("Unexpected RuntimeException occurred", ex);

        return ResponseEntity
            .status(500)
            .body("Váratlan hiba történt!");
    }
}

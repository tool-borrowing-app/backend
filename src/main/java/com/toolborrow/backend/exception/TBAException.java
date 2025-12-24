package com.toolborrow.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TBAException extends RuntimeException {

    private final HttpStatus status;

    public TBAException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}

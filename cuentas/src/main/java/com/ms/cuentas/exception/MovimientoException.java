package com.ms.cuentas.exception;

import org.springframework.http.HttpStatus;

public class MovimientoException extends RuntimeException {

    private final HttpStatus status;

    public MovimientoException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}

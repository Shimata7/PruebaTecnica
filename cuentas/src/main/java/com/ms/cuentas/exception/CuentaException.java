package com.ms.cuentas.exception;

import org.springframework.http.HttpStatus;

public class CuentaException extends RuntimeException {

    private final HttpStatus status;

    public CuentaException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}

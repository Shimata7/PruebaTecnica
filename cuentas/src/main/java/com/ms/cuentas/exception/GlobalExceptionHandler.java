package com.ms.cuentas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovimientoException.class)
    public ResponseEntity<Map<String, String>> manejarMovimientoException(MovimientoException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", ex.getMessage());
        response.put("error", "true");
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(CuentaException.class)
    public ResponseEntity<Map<String, String>> manejarCuentaException(CuentaException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", ex.getMessage());
        response.put("error", "true");
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErrorGlobal(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Error interno: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
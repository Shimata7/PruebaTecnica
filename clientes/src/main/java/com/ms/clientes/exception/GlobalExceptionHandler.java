package com.ms.clientes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClienteException.class)
    public ResponseEntity<Map<String, String>> manejarClienteExiste(ClienteException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", ex.getMessage());
        response.put("error", "true");
        return new ResponseEntity<>(response, ex.getStatus()); // 400
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErrorGlobal(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Ocurrió un error interno en el servidor: " + ex.getMessage());
        response.put("error", "true");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}

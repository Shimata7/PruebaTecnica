package com.ms.cuentas.controller;

import com.ms.cuentas.dto.ReporteDto;
import com.ms.cuentas.entity.Movimiento;
import com.ms.cuentas.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<Movimiento> registrar(
            @RequestBody Movimiento movimiento,
            @RequestParam String numeroCuenta) {
        return new ResponseEntity<>(movimientoService.registrarMovimiento(movimiento, numeroCuenta),
                HttpStatus.CREATED);
    }

    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteDto>> reporte(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaFin,
            @RequestParam Long clienteId) {

        LocalDateTime inicio = fechaInicio.atStartOfDay();

        LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

        List<ReporteDto> reporte = movimientoService.obtenerReporte(inicio, fin, clienteId);

        return ResponseEntity.ok(reporte);
    }
}
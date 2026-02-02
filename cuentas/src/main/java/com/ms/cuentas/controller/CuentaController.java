package com.ms.cuentas.controller;

import com.ms.cuentas.entity.Cuenta;
import com.ms.cuentas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> listar() {
        return ResponseEntity.ok(cuentaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta) {
        return new ResponseEntity<>(cuentaService.createCuenta(cuenta), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @GetMapping("/Cliente/{id}")
    public ResponseEntity<List<Cuenta>> obtenerPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorClienteId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> actualizar(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.updateCuenta(id, cuenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }
}
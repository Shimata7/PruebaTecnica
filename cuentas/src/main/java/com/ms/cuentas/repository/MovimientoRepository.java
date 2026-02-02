package com.ms.cuentas.repository;

import com.ms.cuentas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaClienteIdAndFechaBetween(Long clienteId, LocalDateTime fechaInicio,
            LocalDateTime fechaFin);
}
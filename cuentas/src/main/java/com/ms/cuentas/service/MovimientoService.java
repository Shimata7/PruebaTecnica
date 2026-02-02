package com.ms.cuentas.service;

import com.ms.cuentas.client.ClienteFeignClient;
import com.ms.cuentas.dto.ClienteDto;
import com.ms.cuentas.dto.ReporteDto;
import com.ms.cuentas.entity.Cuenta;
import com.ms.cuentas.entity.Movimiento;
import com.ms.cuentas.enums.TipoMovimiento;
import com.ms.cuentas.exception.MovimientoException;
import com.ms.cuentas.repository.CuentaRepository;
import com.ms.cuentas.repository.MovimientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteFeignClient clienteClient;

    TipoMovimiento tipoMovimiento;

    public List<ReporteDto> obtenerReporte(LocalDateTime inicio, LocalDateTime fin, Long clienteId) {

        List<Movimiento> movimientos = movimientoRepository.findByCuentaClienteIdAndFechaBetween(clienteId, inicio,
                fin);

        if (movimientos.isEmpty()) {
            return List.of();
        }
        String nombreCliente;
        try {
            ClienteDto clienteDto = clienteClient.obtenerCliente(clienteId);
            nombreCliente = clienteDto.getNombre();
        } catch (Exception e) {
            throw new MovimientoException("No se encuentra cliente", HttpStatus.NOT_FOUND);
        }

        final String clienteFinal = nombreCliente;

        return movimientos.stream().map(mov -> {
            BigDecimal saldoInicialTransaccion = mov.getSaldo().subtract(mov.getValor());

            return new ReporteDto(
                    mov.getFecha(),
                    clienteFinal,
                    mov.getCuenta().getNumeroCuenta(),
                    mov.getCuenta().getTipoCuenta().toString(),
                    saldoInicialTransaccion,
                    mov.getCuenta().isEstado(),
                    mov.getValor(),
                    mov.getSaldo());
        }).collect(Collectors.toList());
    }

    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimientoRequest, String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new MovimientoException("Cuenta no encontrada: " + numeroCuenta,
                        HttpStatus.NOT_FOUND));

        if (!cuenta.isEstado()) {
            throw new MovimientoException("Cuenta no está activa: " + numeroCuenta, HttpStatus.NOT_FOUND);
        }

        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal valorMovimiento = movimientoRequest.getValor();
        BigDecimal nuevoSaldo = saldoActual.add(valorMovimiento);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new MovimientoException("Saldo no disponible o insuficiente: " + saldoActual.toString(),
                    HttpStatus.NOT_FOUND);
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimientoRequest.setSaldo(nuevoSaldo);
        movimientoRequest.setCuenta(cuenta);
        movimientoRequest.setFecha(LocalDateTime.now());

        movimientoRequest.setTipoMovimiento(
                valorMovimiento.compareTo(BigDecimal.ZERO) > 0 ? tipoMovimiento.DEPOSITO : tipoMovimiento.RETIRO);

        return movimientoRepository.save(movimientoRequest);
    }
}
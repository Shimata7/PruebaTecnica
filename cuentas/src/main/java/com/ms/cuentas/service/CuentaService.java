package com.ms.cuentas.service;

import org.springframework.http.HttpStatus;
import com.ms.cuentas.entity.Cuenta;
import com.ms.cuentas.exception.CuentaException;
import com.ms.cuentas.repository.CuentaRepository;
import com.ms.cuentas.client.ClienteFeignClient;
import com.ms.cuentas.dto.ClienteDto;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteFeignClient clienteClient;

    public Cuenta createCuenta(Cuenta cuenta) {
        ClienteDto cliente;
        try {
            cliente = clienteClient.obtenerCliente(cuenta.getClienteId());
        } catch (FeignException.NotFound e) {
            throw new CuentaException("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        if (!cliente.isEstado()) {
            throw new CuentaException("Cliente no encontrado o inactivo", HttpStatus.NOT_FOUND);
        }

        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.findAll();
    }

    public Cuenta obtenerPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaException("Cuenta no encontrada", HttpStatus.NOT_FOUND));
    }

    public List<Cuenta> obtenerPorClienteId(Long id) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(id);
        if (cuentas.isEmpty()) {
            throw new CuentaException("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
        return cuentas;
    }

    public Cuenta updateCuenta(Long id, Cuenta cuentaInfo) {
        Cuenta cuenta = obtenerPorId(id);

        cuenta.setTipoCuenta(cuentaInfo.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaInfo.getSaldoInicial());
        cuenta.setEstado(cuentaInfo.isEstado());
        return cuentaRepository.save(cuenta);
    }

    public void deleteCuenta(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(false);
        cuentaRepository.save(cuenta);
    }
}

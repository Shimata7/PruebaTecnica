package com.ms.cuentas.messaging;

import com.ms.cuentas.entity.Cuenta;
import com.ms.cuentas.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteEventConsumer {

    @Autowired
    private CuentaRepository cuentaRepository;

    @KafkaListener(topics = "cliente-eliminado-topic", groupId = "grupo-cuentas")
    @Transactional
    public void recibirEventoEliminacion(Long clienteId) {

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (!cuentas.isEmpty()) {
            cuentas.forEach(c -> c.setEstado(false));
            cuentaRepository.saveAll(cuentas);
        }
    }
}

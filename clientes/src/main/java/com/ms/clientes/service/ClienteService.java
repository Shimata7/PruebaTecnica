package com.ms.clientes.service;

import com.ms.clientes.entity.Cliente;
import com.ms.clientes.exception.ClienteException;
import com.ms.clientes.repository.ClienteRepository;
import com.ms.clientes.dto.ClienteDto;
import com.ms.clientes.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ClienteMapper clienteMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Cliente createCliente(Cliente cliente) {

        if (clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new ClienteException("Ya existe un cliente con la misma identificación",
                    HttpStatus.BAD_REQUEST);
        }

        String hashPwd = passwordEncoder.encode(cliente.getContrasena());
        cliente.setContrasena(hashPwd);

        if (!cliente.isEstado()) {
            cliente.setEstado(true);
        }

        Cliente nuevoCliente = clienteRepository.save(cliente);

        return nuevoCliente;
    }

    @Transactional
    public Cliente updateCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteException("Cliente no encontrado", HttpStatus.BAD_REQUEST));

        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setGenero(clienteActualizado.getGenero());
        clienteExistente.setEdad(clienteActualizado.getEdad());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        if (clienteActualizado.getContrasena() != null && !clienteActualizado.getContrasena().isEmpty()) {
            clienteExistente.setContrasena(passwordEncoder.encode(clienteActualizado.getContrasena()));
        }

        return clienteRepository.save(clienteExistente);
    }

    public List<ClienteDto> obtenerTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> clienteMapper.toDto(cliente))
                .collect(Collectors.toList());
    }

    public ClienteDto obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteException("Cliente no encontrado", HttpStatus.BAD_REQUEST));

        return clienteMapper.toDto(cliente);
    }

    private static final String TOPIC_ELIMINADO = "cliente-eliminado-topic";

    @Transactional
    public void deleteCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteException("Cliente no encontrado", HttpStatus.BAD_REQUEST));

        cliente.setEstado(false);

        clienteRepository.save(cliente);

        try {
            kafkaTemplate.send(TOPIC_ELIMINADO, id);
        } catch (Exception e) {
            System.err.println("Error enviando evento de eliminación: " + e.getMessage());
        }
    }
}

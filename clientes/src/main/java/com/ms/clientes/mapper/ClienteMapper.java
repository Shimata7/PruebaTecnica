package com.ms.clientes.mapper;

import com.ms.clientes.dto.ClienteDto;
import com.ms.clientes.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    public ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteDto dto = new ClienteDto();

        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setEstado(cliente.isEstado());

        return dto;
    }
}

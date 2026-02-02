package com.ms.clientes.repository;

import com.ms.clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByIdentificacion(String identificacion);
}

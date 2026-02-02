package com.ms.cuentas.client;

import com.ms.cuentas.dto.ClienteDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-clientes", url = "http://ms-clientes:8081/clientes")
public interface ClienteFeignClient {

    @GetMapping("/{id}")
    ClienteDto obtenerCliente(@PathVariable Long id);
}
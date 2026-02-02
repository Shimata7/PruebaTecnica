package com.ms.clientes.controller;

import com.ms.clientes.dto.ClienteDto;
import com.ms.clientes.entity.Cliente;
import com.ms.clientes.mapper.ClienteMapper;
import com.ms.clientes.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClienteMapper clienteMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Integración: Debe retornar 201 Created al crear un cliente válido")
    void crearClienteIntegrationTest() throws Exception {

        Cliente clienteInput = new Cliente();
        clienteInput.setNombre("Juan Test");
        clienteInput.setContrasena("123");
        clienteInput.setEstado(true);

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Juan Test");
        clienteGuardado.setEstado(true);

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(1L);
        clienteDto.setNombre("Juan Test");
        clienteDto.setEstado(true);

        when(clienteService.createCliente(any(Cliente.class))).thenReturn(clienteGuardado);
        when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInput)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Test"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value(true));
    }
}
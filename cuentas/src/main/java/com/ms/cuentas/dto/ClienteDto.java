package com.ms.cuentas.dto;

import lombok.Data;

@Data
public class ClienteDto {
    private Long id;
    private String nombre;
    private boolean estado;
}
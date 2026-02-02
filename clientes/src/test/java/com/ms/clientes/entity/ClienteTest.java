package com.ms.clientes.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    @DisplayName("Debe crear un cliente correctamente y heredar atributos de Persona")
    void testClienteCreation() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Jose Lema");
        cliente.setEdad(30);
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        String nombreObtenido = cliente.getNombre();
        boolean estadoObtenido = cliente.isEstado();

        Assertions.assertEquals("Jose Lema", nombreObtenido, "El nombre no coincide");
        Assertions.assertEquals(30, cliente.getEdad(), "La edad no coincide");
        Assertions.assertTrue(estadoObtenido, "El estado debería ser verdadero");

        Assertions.assertInstanceOf(Persona.class, cliente);
    }
}
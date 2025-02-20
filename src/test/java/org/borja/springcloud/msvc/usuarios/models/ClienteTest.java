package org.borja.springcloud.msvc.usuarios.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClienteTest {

    @Test
    @DisplayName("Test Crear Cliente")
    public void testCrearCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Perez");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        assertNotNull(cliente);
        assertEquals("Juan Perez", cliente.getNombre());
        assertTrue(cliente.getEstado());
    }
}
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
        Client cliente = new Client();
        cliente.setName("Juan Perez");
        cliente.setPassword("1234");
        cliente.setStatus(true);

        assertNotNull(cliente);
        assertEquals("Juan Perez", cliente.getName());
        assertTrue(cliente.getStatus());
    }
}
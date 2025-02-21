package org.borja.springcloud.msvc.usuarios.models;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.borja.springcloud.msvc.usuarios.controllers.ClienteController;
import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteResponseDto;
import org.borja.springcloud.msvc.usuarios.services.cliente.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearCliente() throws Exception {
        // Configurar datos de entrada para la creación del cliente usando builder
        ClienteRequestDto requestDto = ClienteRequestDto.builder()
                .nombre("Cliente de Prueba")
                .genero("Masculino")
                .edad(30)
                .identificacion("12345678")
                .direccion("Calle Falsa 123")
                .telefono("987654321")
                .clienteId("cliente-001")
                .contrasena("password")
                .estado(true)
                .build();

        // Configurar respuesta simulada del servicio usando builder
        ClienteResponseDto responseDto = ClienteResponseDto.builder()
                .id(1L)
                .nombre("Cliente de Prueba")
                .genero("Masculino")
                .edad(30)
                .identificacion("12345678")
                .direccion("Calle Falsa 123")
                .telefono("987654321")
                .clienteId("cliente-001")
                .estado(true)
                .build();

        // Simular el comportamiento del método crearCliente del servicio
        Mockito.when(clienteService.crearCliente(Mockito.any(ClienteRequestDto.class)))
                .thenReturn(responseDto);

        // Realizar la petición POST y validar la respuesta
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Cliente creado exitosamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Cliente de Prueba"));
    }

    @Test
    public void testObtenerClientePorId() throws Exception {
        Long clienteId = 1L;

        // Configurar respuesta simulada del servicio usando builder
        ClienteResponseDto responseDto = ClienteResponseDto.builder()
                .id(clienteId)
                .nombre("Cliente de Prueba")
                .genero("Masculino")
                .edad(30)
                .identificacion("12345678")
                .direccion("Calle Falsa 123")
                .telefono("987654321")
                .clienteId("cliente-001")
                .estado(true)
                .build();

        // Simular el comportamiento del método obtenerPorId del servicio
        Mockito.when(clienteService.obtenerPorId(clienteId))
                .thenReturn(responseDto);

        // Realizar la petición GET y validar la respuesta
        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cliente encontrado"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nombre").value("Cliente de Prueba"));
    }
}

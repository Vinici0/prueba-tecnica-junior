package org.borja.springcloud.msvc.usuarios.models;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.borja.springcloud.msvc.usuarios.controllers.ClientController;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientResponseDto;
import org.borja.springcloud.msvc.usuarios.models.enums.Gender;
import org.borja.springcloud.msvc.usuarios.services.client.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClientController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateClient() throws Exception {
        // Configure input data for client creation using builder
        ClientRequestDto requestDto = ClientRequestDto.builder()
                .name("Test Client")
                .gender(Gender.valueOf("MALE"))
                .age(30)
                .identification("12345678")
                .address("123 Fake Street")
                .phone("987654321")
                .clientId("client-001")
                .password("password")
                .status(true)
                .build();

        // Configure simulated service response using builder
        ClientResponseDto responseDto = ClientResponseDto.builder()
                .id(1L)
                .name("Test Client")
                .gender(Gender.valueOf("MALE"))
                .age(30)
                .identification("12345678")
                .address("123 Fake Street")
                .phone("987654321")
                .clientId("client-001")
                .status(true)
                .build();

        Mockito.when(clientService.addClient(Mockito.any(ClientRequestDto.class)))
                .thenReturn(responseDto);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Client created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Client"));
    }

    @Test
    public void testGetClientById() throws Exception {
        Long clientId = 1L;

        // Configure simulated service response using builder
        ClientResponseDto responseDto = ClientResponseDto.builder()
                .id(clientId)
                .name("Test Client")
                .gender(Gender.valueOf("MALE"))
                .age(30)
                .identification("12345678")
                .address("123 Fake Street")
                .phone("987654321")
                .clientId("client-001")
                .status(true)
                .build();

        // Simulate the behavior of the service's getById method
        Mockito.when(clientService.getClientById(clientId))
                .thenReturn(responseDto);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Client found"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Client"));
    }
}
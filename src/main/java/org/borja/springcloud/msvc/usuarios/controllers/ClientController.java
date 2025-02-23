package org.borja.springcloud.msvc.usuarios.controllers;

// Java core imports
import java.util.List;

// Spring framework imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// Application imports
import org.borja.springcloud.msvc.usuarios.dto.client.ClientRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientResponseDto;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;
import org.borja.springcloud.msvc.usuarios.services.client.IClientService;


@RestController
@RequestMapping("${api.prefix}/clientes")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse> addClient(@Validated @RequestBody ClientRequestDto clientDto) {
        ClientResponseDto newClient = clientService.addClient(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Cliente creado exitosamente", newClient));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllClients() {
        List<ClientResponseDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(new ApiResponse("Lista de clientes recuperada", clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getClientById(@PathVariable Long id) {
        ClientResponseDto client = clientService.getClientById(id);
        return ResponseEntity.ok(new ApiResponse("Cliente encontrado", client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateClient(@PathVariable Long id,
                                                    @Validated @RequestBody ClientRequestDto clientDto) {
        ClientResponseDto updatedClient = clientService.updateClient(id, clientDto);
        return ResponseEntity.ok(new ApiResponse("Cliente actualizado exitosamente", updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(new ApiResponse("Cliente eliminado exitosamente", null));
    }
}
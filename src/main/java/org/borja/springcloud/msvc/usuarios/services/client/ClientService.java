package org.borja.springcloud.msvc.usuarios.services.client;

// Java core imports
import java.util.List;
import java.util.stream.Collectors;

// Lombok imports
import lombok.RequiredArgsConstructor;

// Spring framework imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Application imports
import org.borja.springcloud.msvc.usuarios.dto.client.ClientRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Client;
import org.borja.springcloud.msvc.usuarios.repositories.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ClientResponseDto addClient(ClientRequestDto clientDto) {
        List<Client> existingClients = clientRepository.findByIdentification(clientDto.getIdentification());

        if (!existingClients.isEmpty()) {
            log.warn("Client already exists with identification: {}", clientDto.getIdentification());
            throw new ResourceNotFoundException(
                    "Cliente ya existe con identificación: " + clientDto.getIdentification());
        }

        log.info("Adding new client: {}", clientDto.getName());
        Client client = convertToEntity(clientDto);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Client savedClient = clientRepository.save(client);
        log.info("Client created successfully with ID: {}", savedClient.getId());
        return convertToDto(savedClient);
    }

    @Override
    public List<ClientResponseDto> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAllByStatus(true)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDto getClientById(Long id) {
        log.info("Fetching client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found with ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con id: " + id);
                });
        return convertToDto(client);
    }

    @Override
    public ClientResponseDto updateClient(Long id, ClientRequestDto clientDto) {
        log.info("Updating client with ID: {}", id);
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found with ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con id: " + id);
                });

        updateEntityFromDto(existingClient, clientDto);
        if (clientDto.getPassword() != null && !clientDto.getPassword().isEmpty()) {
            existingClient.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        }
        Client savedClient = clientRepository.save(existingClient);
        log.info("Client updated successfully with ID: {}", savedClient.getId());
        return convertToDto(savedClient);
    }

    @Override
    public void deleteClient(Long id) {
        log.info("Disabling client with ID: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Client not found with ID: {}", id);
                    return new ResourceNotFoundException("Cliente no encontrado con id: " + id);
                });
        client.setStatus(false);
        clientRepository.save(client);
        log.info("Client disabled successfully with ID: {}", id);
    }

    private Client convertToEntity(ClientRequestDto dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setPassword(dto.getPassword());
        return client;
    }

    private ClientResponseDto convertToDto(Client client) {
        return ClientResponseDto.builder()
                .id(client.getId())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phone(client.getPhone())
                .clientId(client.getClientId())
                .build();
    }

    private void updateEntityFromDto(Client client, ClientRequestDto dto) {
        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            client.setPassword(dto.getPassword());
        }
    }
}

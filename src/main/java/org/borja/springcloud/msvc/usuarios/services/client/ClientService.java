package org.borja.springcloud.msvc.usuarios.services.client;

import lombok.RequiredArgsConstructor;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.DuplicateKeyException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Client;
import org.borja.springcloud.msvc.usuarios.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ClientResponseDto addClient(ClientRequestDto clientDto) {
        Client client = convertToEntity(clientDto);
        if (clientRepository.findByClientId(clientDto.getClientId()).isPresent()) {
            throw new DuplicateKeyException("Ya existe un cliente con el ID: " + clientDto.getClientId());
        }
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Client savedCliente = clientRepository.save(client);
        return convertToDto(savedCliente);
    }

    @Override
    public List<ClientResponseDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDto getClientById(Long id) {
        Client cliente = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDto(cliente);
    }

    @Override
    public ClientResponseDto updateClient(Long id, ClientRequestDto clientDto) {
        Client clienteExistente = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        updateEntityFromDto(clienteExistente, clientDto);
        if (clientDto.getPassword() != null && !clientDto.getPassword().isEmpty()) {
            clienteExistente.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        }
        Client savedCliente = clientRepository.save(clienteExistente);
        return convertToDto(savedCliente);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clientRepository.deleteById(id);
    }

    private Client convertToEntity(ClientRequestDto dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setGender(dto.getGender());
        client.setAge(dto.getAge());
        client.setIdentification(dto.getIdentification());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setClientId(dto.getClientId());
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
        client.setClientId(dto.getClientId());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            client.setPassword(dto.getPassword());
        }
    }
}
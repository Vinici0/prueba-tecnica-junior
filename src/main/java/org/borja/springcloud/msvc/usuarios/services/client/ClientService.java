package org.borja.springcloud.msvc.usuarios.services.client;

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
public class ClientService implements IClientService {

    private final ClientRepository clienteRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Autowired
    public ClientService(ClientRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClientResponseDto addClient(ClientRequestDto clienteDto) {
        Client cliente = convertToEntity(clienteDto);
        if (clienteRepository.findByClientId(clienteDto.getClientId()).isPresent()) {
            throw new DuplicateKeyException("Ya existe un cliente con el ID: " + clienteDto.getClientId());
        }
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        Client savedCliente = clienteRepository.save(cliente);
        return convertToDto(savedCliente);
    }

    @Override
    public List<ClientResponseDto> getAllClients() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponseDto getClientById(Long id) {
        Client cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDto(cliente);
    }

    @Override
    public ClientResponseDto updateClient(Long id, ClientRequestDto clienteDto) {
        Client clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        updateEntityFromDto(clienteExistente, clienteDto);
        if (clienteDto.getPassword() != null && !clienteDto.getPassword().isEmpty()) {
            clienteExistente.setPassword(passwordEncoder.encode(clienteDto.getPassword()));
        }
        Client savedCliente = clienteRepository.save(clienteExistente);
        return convertToDto(savedCliente);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
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
        client.setStatus(dto.getStatus());
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
                .status(client.getStatus())
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
        client.setStatus(dto.getStatus());
    }
}
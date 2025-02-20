package org.borja.springcloud.msvc.usuarios.services.cliente;

import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Cliente;
import org.borja.springcloud.msvc.usuarios.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    public ClienteResponseDto crearCliente(ClienteRequestDto clienteDto) {
        Cliente cliente = convertToEntity(clienteDto);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDto(savedCliente);
    }


    public List<ClienteResponseDto> obtenerTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public ClienteResponseDto obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDto(cliente);
    }


    public ClienteResponseDto actualizarCliente(Long id, ClienteRequestDto clienteDto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        updateEntityFromDto(clienteExistente, clienteDto);
        Cliente savedCliente = clienteRepository.save(clienteExistente);
        return convertToDto(savedCliente);
    }


    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private Cliente convertToEntity(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado());
        return cliente;
    }

    private ClienteResponseDto convertToDto(Cliente cliente) {
        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .clienteId(cliente.getClienteId())
                .estado(cliente.getEstado())
                .build();
    }

    private void updateEntityFromDto(Cliente cliente, ClienteRequestDto dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            cliente.setContrasena(dto.getContrasena());
        }
        cliente.setEstado(dto.getEstado());
    }
}
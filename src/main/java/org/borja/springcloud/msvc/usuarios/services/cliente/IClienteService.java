package org.borja.springcloud.msvc.usuarios.services.cliente;

import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cliente.ClienteResponseDto;

import java.util.List;

public interface IClienteService {

    ClienteResponseDto crearCliente(ClienteRequestDto clienteDto);

    List<ClienteResponseDto> obtenerTodos();

    ClienteResponseDto obtenerPorId(Long id);

    ClienteResponseDto actualizarCliente(Long id, ClienteRequestDto clienteDto);

    void eliminarCliente(Long id);
}
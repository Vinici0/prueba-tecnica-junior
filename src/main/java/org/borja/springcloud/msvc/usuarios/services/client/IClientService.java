package org.borja.springcloud.msvc.usuarios.services.client;

import org.borja.springcloud.msvc.usuarios.dto.client.ClientRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.client.ClientResponseDto;

import java.util.List;

public interface IClientService {

    ClientResponseDto addClient(ClientRequestDto clientDto);

    List<ClientResponseDto> getAllClients();

    ClientResponseDto getClientById(Long id);

    ClientResponseDto updateClient(Long id, ClientRequestDto clientDto);

    void deleteClient(Long id);
}
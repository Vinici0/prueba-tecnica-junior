package org.borja.springcloud.msvc.usuarios.services;

import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Cliente;
import org.borja.springcloud.msvc.usuarios.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    public Cliente actualizarCliente(Long id, Cliente datosActualizados) {
        Cliente clienteExistente = obtenerPorId(id);

        clienteExistente.setNombre(datosActualizados.getNombre());
        clienteExistente.setContrasena(datosActualizados.getContrasena());
        clienteExistente.setEstado(datosActualizados.getEstado());

        return clienteRepository.save(clienteExistente);
    }

    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerPorId(id);
        clienteRepository.delete(cliente);
    }
}
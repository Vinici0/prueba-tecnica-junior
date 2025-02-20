package org.borja.springcloud.msvc.usuarios.services.cuenta;

import jakarta.transaction.Transactional;
import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Cliente;
import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.repositories.ClienteRepository;
import org.borja.springcloud.msvc.usuarios.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService implements ICuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public CuentaResponseDto crearCuenta(CuentaRequestDto cuentaDto) {
        Cliente cliente = clienteRepository.findById(cuentaDto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + cuentaDto.getClienteId()));

        System.out.println("Cliente: " + cliente);
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDto.getSaldoInicial());
        cuenta.setEstado(cuentaDto.getEstado());
        cuenta.setCliente(cliente);

        cuenta = cuentaRepository.save(cuenta);
        return mapToResponseDto(cuenta);
    }

    @Override
    @Transactional
    public List<CuentaResponseDto> obtenerTodas() {
        return cuentaRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CuentaResponseDto obtenerPorNumero(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));
        return mapToResponseDto(cuenta);
    }

    @Override
    @Transactional
    public CuentaResponseDto actualizarCuenta(String numeroCuenta, CuentaRequestDto cuentaDto) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));

        Cliente cliente = clienteRepository.findById(cuentaDto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + cuentaDto.getClienteId()));

        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDto.getSaldoInicial());
        cuenta.setEstado(cuentaDto.getEstado());
        cuenta.setCliente(cliente);

        cuenta = cuentaRepository.save(cuenta);
        return mapToResponseDto(cuenta);
    }

    @Override
    @Transactional
    public void eliminarCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + numeroCuenta));
        cuentaRepository.delete(cuenta);
    }

    private CuentaResponseDto mapToResponseDto(Cuenta cuenta) {
        return CuentaResponseDto.builder()
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .clienteId(cuenta.getCliente().getId())
                .build();
    }
}
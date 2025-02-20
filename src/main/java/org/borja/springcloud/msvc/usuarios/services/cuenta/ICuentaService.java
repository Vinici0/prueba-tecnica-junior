package org.borja.springcloud.msvc.usuarios.services.cuenta;

import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaResponseDto;

import java.util.List;

public interface ICuentaService {
    CuentaResponseDto crearCuenta(CuentaRequestDto cuentaDto);
    List<CuentaResponseDto> obtenerTodas();
    CuentaResponseDto obtenerPorNumero(String numeroCuenta);
    CuentaResponseDto actualizarCuenta(String numeroCuenta, CuentaRequestDto cuentaDto);
    void eliminarCuenta(String numeroCuenta);
}

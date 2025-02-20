package org.borja.springcloud.msvc.usuarios.dto.cuenta;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuentaResponseDto {
    private String numeroCuenta;
    private String tipoCuenta;
    private Double saldoInicial;
    private Boolean estado;
    private Long clienteId;
}
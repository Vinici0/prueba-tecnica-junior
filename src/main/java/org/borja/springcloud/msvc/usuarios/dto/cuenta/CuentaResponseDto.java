package org.borja.springcloud.msvc.usuarios.dto.cuenta;

import lombok.Builder;
import lombok.Data;
import org.borja.springcloud.msvc.usuarios.models.enums.TipoCuenta;

@Data
@Builder
public class CuentaResponseDto {
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private Double saldoInicial;
    private Boolean estado;
    private Long clienteId;
}
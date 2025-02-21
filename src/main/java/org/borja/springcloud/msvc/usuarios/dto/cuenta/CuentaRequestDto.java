package org.borja.springcloud.msvc.usuarios.dto.cuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.borja.springcloud.msvc.usuarios.models.enums.TipoCuenta;

@Data
@Builder
public class CuentaRequestDto {

    @NotBlank
    private String numeroCuenta;

    @NotNull
    private TipoCuenta tipoCuenta;

    @NotNull
    private Double saldoInicial;

    @NotNull
    private Boolean estado;

    @Positive
    private Long clienteId;
}
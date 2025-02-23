package org.borja.springcloud.msvc.usuarios.dto.movement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementRequestDto {

    @NotBlank
    private String tipoMovimiento;

    @NotNull
    private Double valor;

    @NotBlank
    private String numeroCuenta;
}
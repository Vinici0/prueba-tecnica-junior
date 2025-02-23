package org.borja.springcloud.msvc.usuarios.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovementReportDetailDto {
    private LocalDate fecha;
    private String tipoMovimiento;
    private Double movimiento;
    private Double saldoDisponible;
}
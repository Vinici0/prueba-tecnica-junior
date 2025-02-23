package org.borja.springcloud.msvc.usuarios.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto implements Serializable {
    private String fecha;
    private String cliente;
    private List<AccountReportDto> cuentas;
}
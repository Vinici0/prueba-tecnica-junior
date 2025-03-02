package org.borja.springcloud.msvc.usuarios.controllers;


// Java core imports
import java.time.LocalDate;
import java.util.List;

// Spring framework imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Application imports
import org.borja.springcloud.msvc.usuarios.repositories.interfaces.MovementReportProjection;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;
import org.borja.springcloud.msvc.usuarios.services.movement.IMovementService;


@RestController
@RequestMapping("${api.prefix}/reportes")
public class ReportController {

    @Autowired
    private IMovementService movementService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCustomReport(
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        System.out.println("clienteId: " + clienteId + " fechaInicio: " + fechaInicio + " fechaFin: " + fechaFin);
        List<MovementReportProjection> report = movementService.getCustomReport(fechaInicio, fechaFin,
                clienteId);
        return ResponseEntity.ok(new ApiResponse("Reporte de estado de cuenta", report));
    }
}
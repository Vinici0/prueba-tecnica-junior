package org.borja.springcloud.msvc.usuarios.controllers;

import org.borja.springcloud.msvc.usuarios.dto.movement.ReportDto;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;
import org.borja.springcloud.msvc.usuarios.services.movement.IMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("${api.prefix}/reportes")
public class ReportController {

    @Autowired
    private IMovementService movementService;

    @GetMapping
    public ResponseEntity<ApiResponse> generateReport(
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
            
        ReportDto report = movementService.generateReport(clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(new ApiResponse("Reporte de estado de cuenta", report));
    }
}
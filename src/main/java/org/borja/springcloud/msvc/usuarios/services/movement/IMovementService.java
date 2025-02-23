package org.borja.springcloud.msvc.usuarios.services.movement;

import org.borja.springcloud.msvc.usuarios.dto.movement.MovementRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.movement.MovementResponseDto;
import org.borja.springcloud.msvc.usuarios.dto.movement.ReportDto;
import org.borja.springcloud.msvc.usuarios.dto.report.ReportResponseDto;
import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.repositories.interfaces.MovementReportProjection;

import java.time.LocalDate;
import java.util.List;

public interface IMovementService {

    MovementResponseDto addMovement(MovementRequestDto movRequest);

    List<MovementResponseDto> getAllMovements();

    MovementResponseDto getMovementById(Long id);

    MovementResponseDto updateMovement(Long id, MovementRequestDto movRequest);

    void deleteMovement(Long id);

    List<MovementReportProjection> getCustomReport(LocalDate startDate, LocalDate endDate, Long clientId);
}
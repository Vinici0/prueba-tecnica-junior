package org.borja.springcloud.msvc.usuarios.services.movement;

import org.borja.springcloud.msvc.usuarios.dto.movement.ReportDto;
import org.borja.springcloud.msvc.usuarios.models.Movement;

import java.time.LocalDate;
import java.util.List;

public interface IMovementService {
    Movement addMovement(Movement movRequest);

    List<Movement> getAllMovements();

    Movement getMovementById(Long id);

    Movement updateMovement(Long id, Movement data);

    void deleteMovement(Long id);

    ReportDto generateReport(Long clientId, LocalDate startDate, LocalDate endDate);
}
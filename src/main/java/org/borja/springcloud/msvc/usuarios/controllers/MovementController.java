package org.borja.springcloud.msvc.usuarios.controllers;

import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;
import org.borja.springcloud.msvc.usuarios.services.movement.IMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/movimientos")
public class MovementController {

    @Autowired
    private IMovementService movementService;

    @PostMapping
    public ResponseEntity<ApiResponse> addMovement(@Validated @RequestBody Movement movRequest) {
        Movement newMovement = movementService.addMovement(movRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Movimiento creado exitosamente", newMovement));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMovements() {
        List<Movement> movements = movementService.getAllMovements();
        return ResponseEntity.ok(new ApiResponse("Lista de movimientos recuperada", movements));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMovementById(@PathVariable Long id) {
        Movement movement = movementService.getMovementById(id);
        return ResponseEntity.ok(new ApiResponse("Movimiento encontrado", movement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMovement(
            @PathVariable Long id,
            @Validated @RequestBody Movement datos) {
        Movement updateMovement = movementService.updateMovement(id, datos);
        return ResponseEntity.ok(new ApiResponse("Movimiento actualizado exitosamente", updateMovement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.ok(new ApiResponse("Movimiento eliminado exitosamente", null));
    }
}
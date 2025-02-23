package org.borja.springcloud.msvc.usuarios.controllers;

import org.borja.springcloud.msvc.usuarios.dto.movement.MovementRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.movement.MovementResponseDto;
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
    public ResponseEntity<ApiResponse> addMovement(@Validated @RequestBody MovementRequestDto movRequest) {
        MovementResponseDto responseDto = movementService.addMovement(movRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Movimiento creado exitosamente", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMovements() {
        List<MovementResponseDto> movements = movementService.getAllMovements();
        return ResponseEntity.ok(new ApiResponse("Lista de movimientos recuperada", movements));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMovementById(@PathVariable Long id) {
        MovementResponseDto responseDto = movementService.getMovementById(id);
        return ResponseEntity.ok(new ApiResponse("Movimiento encontrado", responseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMovement(
            @PathVariable Long id,
            @Validated @RequestBody MovementRequestDto movRequest) {
        MovementResponseDto responseDto = movementService.updateMovement(id, movRequest);
        return ResponseEntity.ok(new ApiResponse("Movimiento actualizado exitosamente", responseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMovement(@PathVariable Long id) {
        movementService.deleteMovement(id);
        return ResponseEntity.ok(new ApiResponse("Movimiento eliminado exitosamente", null));
    }
}
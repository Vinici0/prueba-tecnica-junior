package org.borja.springcloud.msvc.usuarios.controllers;

import java.util.List;

import org.borja.springcloud.msvc.usuarios.services.cuenta.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.cuenta.CuentaResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<ApiResponse> crear(@Validated @RequestBody CuentaRequestDto cuentaDto) {
        try {
            CuentaResponseDto nueva = cuentaService.crearCuenta(cuentaDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Cuenta creada exitosamente", nueva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listarTodas() {
        try {
            List<CuentaResponseDto> cuentas = cuentaService.obtenerTodas();
            return ResponseEntity.ok(new ApiResponse("Lista de cuentas recuperada", cuentas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> obtenerPorNumero(@PathVariable String numeroCuenta) {
        try {
            CuentaResponseDto cuenta = cuentaService.obtenerPorNumero(numeroCuenta);
            return ResponseEntity.ok(new ApiResponse("Cuenta encontrada", cuenta));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable String numeroCuenta,
            @Validated @RequestBody CuentaRequestDto cuentaDto) {
        try {
            CuentaResponseDto actualizada = cuentaService.actualizarCuenta(numeroCuenta, cuentaDto);
            return ResponseEntity.ok(new ApiResponse("Cuenta actualizada exitosamente", actualizada));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable String numeroCuenta) {
        try {
            cuentaService.eliminarCuenta(numeroCuenta);
            return ResponseEntity.ok(new ApiResponse("Cuenta eliminada exitosamente", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
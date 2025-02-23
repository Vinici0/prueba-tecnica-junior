package org.borja.springcloud.msvc.usuarios.controllers;

import java.util.List;

import org.borja.springcloud.msvc.usuarios.services.account.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.borja.springcloud.msvc.usuarios.dto.account.AccountRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.account.AccountResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cuentas")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse> addAccount(@Validated @RequestBody AccountRequestDto cuentaDto) {
        try {
            AccountResponseDto nueva = accountService.addAccount(cuentaDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Cuenta creada exitosamente", nueva));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAccounts() {
        try {
            List<AccountResponseDto> cuentas = accountService.getAllAccounts();
            return ResponseEntity.ok(new ApiResponse("Lista de cuentas recuperada", cuentas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> getAccountByNumber(@PathVariable String numeroCuenta) {
        try {
            AccountResponseDto cuenta = accountService.getAccountByNumber(numeroCuenta);
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
    public ResponseEntity<ApiResponse> updateAccount(
            @PathVariable String numeroCuenta,
            @Validated @RequestBody AccountRequestDto cuentaDto) {
        try {
            AccountResponseDto actualizada = accountService.updateAccount(numeroCuenta, cuentaDto);
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
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable String numeroCuenta) {
        try {
            accountService.deleteAccount(numeroCuenta);
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
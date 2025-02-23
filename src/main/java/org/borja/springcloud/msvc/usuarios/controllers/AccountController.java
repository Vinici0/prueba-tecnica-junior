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
import org.borja.springcloud.msvc.usuarios.response.ApiResponse;

@RestController
@RequestMapping("${api.prefix}/cuentas")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse> addAccount(@Validated @RequestBody AccountRequestDto cuentaDto) {
        AccountResponseDto nueva = accountService.addAccount(cuentaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Cuenta creada exitosamente", nueva));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAccounts() {
        List<AccountResponseDto> cuentas = accountService.getAllAccounts();
        return ResponseEntity.ok(new ApiResponse("Lista de cuentas recuperada", cuentas));
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> getAccountByNumber(@PathVariable String numeroCuenta) {
        AccountResponseDto cuenta = accountService.getAccountByNumber(numeroCuenta);
        return ResponseEntity.ok(new ApiResponse("Cuenta encontrada", cuenta));
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> updateAccount(
            @PathVariable String numeroCuenta,
            @Validated @RequestBody AccountRequestDto cuentaDto) {
        AccountResponseDto actualizada = accountService.updateAccount(numeroCuenta, cuentaDto);
        return ResponseEntity.ok(new ApiResponse("Cuenta actualizada exitosamente", actualizada));
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable String numeroCuenta) {
        accountService.deleteAccount(numeroCuenta);
        return ResponseEntity.ok(new ApiResponse("Cuenta eliminada exitosamente", null));
    }
}
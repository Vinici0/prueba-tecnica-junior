package org.borja.springcloud.msvc.usuarios.controllers;

import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta) {
        Cuenta nueva = cuentaService.crearCuenta(cuenta);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cuenta> listarTodas() {
        return cuentaService.obtenerTodas();
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtenerPorId(@PathVariable String numeroCuenta) {
        Cuenta cuenta = cuentaService.obtenerPorNumero(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    @PutMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> actualizar(@PathVariable String numeroCuenta, @RequestBody Cuenta datos) {
        Cuenta cuentaActualizada = cuentaService.actualizarCuenta(numeroCuenta, datos);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Void> eliminar(@PathVariable String numeroCuenta) {
        cuentaService.eliminarCuenta(numeroCuenta);
        return ResponseEntity.noContent().build();
    }
}

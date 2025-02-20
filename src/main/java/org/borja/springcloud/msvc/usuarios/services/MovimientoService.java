package org.borja.springcloud.msvc.usuarios.services;

import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientFundsException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.models.Movimiento;
import org.borja.springcloud.msvc.usuarios.repositories.CuentaRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class MovimientoService {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;

    public Movimiento registrarMovimiento(String numeroCuenta, Movimiento movimiento) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        double nuevoSaldo = cuenta.getSaldoInicial() + movimiento.getValor();
        if(nuevoSaldo < 0) {
            throw new InsufficientFundsException("Saldo no disponible");
        }
        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);
        return movimientoRepository.save(movimiento);
    }
}

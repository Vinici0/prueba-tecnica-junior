package org.borja.springcloud.msvc.usuarios.services;

import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientFundsException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.exceptions.SaldoInsuficienteException;
import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.models.Movimiento;
import org.borja.springcloud.msvc.usuarios.repositories.CuentaRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public MovimientoService(MovimientoRepository movimientoRepository,
                             CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }


    public Movimiento crearMovimiento(Movimiento movRequest) {
        String numeroCuenta = movRequest.getCuenta().getNumeroCuenta();
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        double nuevoSaldo = cuenta.getSaldoInicial() + movRequest.getValor();

        if (nuevoSaldo < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movRequest.setFecha(LocalDate.now());
        movRequest.setSaldo(nuevoSaldo);
        movRequest.setCuenta(cuenta);

        return movimientoRepository.save(movRequest);
    }


    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.findAll();
    }

    public Movimiento obtenerPorId(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
    }


    public Movimiento actualizarMovimiento(Long id, Movimiento datos) {
        Movimiento movExistente = obtenerPorId(id);
        movExistente.setTipoMovimiento(datos.getTipoMovimiento());
        movExistente.setValor(datos.getValor());

        return movimientoRepository.save(movExistente);
    }

    public void eliminarMovimiento(Long id) {
        Movimiento mov = obtenerPorId(id);
        movimientoRepository.delete(mov);
    }
}

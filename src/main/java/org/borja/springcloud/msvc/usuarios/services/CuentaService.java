package org.borja.springcloud.msvc.usuarios.services;

import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    @Autowired
    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Cuenta crearCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.findAll();
    }

    public Cuenta obtenerPorNumero(String numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
    }

    public Cuenta actualizarCuenta(String numeroCuenta, Cuenta datos) {
        Cuenta existente = obtenerPorNumero(numeroCuenta);
        existente.setTipoCuenta(datos.getTipoCuenta());
        existente.setSaldoInicial(datos.getSaldoInicial());
        existente.setEstado(datos.getEstado());

        return cuentaRepository.save(existente);
    }

    public void eliminarCuenta(String numeroCuenta) {
        Cuenta cuenta = obtenerPorNumero(numeroCuenta);
        cuentaRepository.delete(cuenta);
    }
}

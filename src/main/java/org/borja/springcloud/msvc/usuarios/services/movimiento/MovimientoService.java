package org.borja.springcloud.msvc.usuarios.services.movimiento;

import org.borja.springcloud.msvc.usuarios.dto.movimiento.ReporteDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.exceptions.SaldoInsuficienteException;
import org.borja.springcloud.msvc.usuarios.models.Cuenta;
import org.borja.springcloud.msvc.usuarios.models.Movimiento;
import org.borja.springcloud.msvc.usuarios.repositories.CuentaRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public ReporteDto generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // 1. Buscar todas las cuentas del cliente
        List<Cuenta> cuentasCliente = cuentaRepository.findAll()
                .stream()
                .filter(c -> c.getCliente().getId().equals(clienteId))
                .collect(Collectors.toList());

        // 2. Para cada cuenta, buscar movimientos en el rango
        List<ReporteDto.CuentaDto> listaCuentasDto = cuentasCliente.stream().map(c -> {
            List<Movimiento> movimientosEnRango = movimientoRepository.findAll()
                    .stream()
                    .filter(m -> m.getCuenta().getNumeroCuenta().equals(c.getNumeroCuenta()))
                    .filter(m -> !m.getFecha().isBefore(fechaInicio) && !m.getFecha().isAfter(fechaFin))
                    .collect(Collectors.toList());

            // Convertir movimientos a DTO
            List<ReporteDto.MovimientoDto> movimientosDto = movimientosEnRango.stream().map(mov ->
                    ReporteDto.MovimientoDto.builder()
                            .fecha(mov.getFecha())
                            .tipoMovimiento(mov.getTipoMovimiento())
                            .valor(mov.getValor())
                            .saldo(mov.getSaldo())
                            .build()
            ).collect(Collectors.toList());

            return ReporteDto.CuentaDto.builder()
                    .numeroCuenta(c.getNumeroCuenta())
                    .saldoActual(c.getSaldoInicial())
                    .movimientos(movimientosDto)
                    .build();
        }).collect(Collectors.toList());

        // 3. Armar el objeto final
        return ReporteDto.builder()
                .clienteId(clienteId)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .cuentas(listaCuentasDto)
                .build();
    }
}

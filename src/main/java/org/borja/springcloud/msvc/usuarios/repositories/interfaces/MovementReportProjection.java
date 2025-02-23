package org.borja.springcloud.msvc.usuarios.repositories.interfaces;

public interface MovementReportProjection {
    String getFecha();
    String getCliente();
    String getNumeroCuenta();
    String getTipo();
    Double getSaldoDisponible();
    Boolean getEstado();
    Double getMovimiento();
    Double getSaldoInicial();
}

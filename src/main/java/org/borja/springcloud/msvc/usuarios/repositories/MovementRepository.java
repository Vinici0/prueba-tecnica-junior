package org.borja.springcloud.msvc.usuarios.repositories;

import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.repositories.interfaces.MovementReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    @Query(value = """
            SELECT
                TO_CHAR(m.date, 'DD/MM/YYYY') AS fecha,
                p.name AS cliente,
                a.account_number AS numeroCuenta,
                a.account_type AS tipo,
                a.initial_balance AS SaldoInicial,
                a.status AS estado,
                m.amount AS movimiento,
                m.balance AS SaldoDisponible
            FROM movements m
                INNER JOIN accounts a ON m.account_id = a.id
                INNER JOIN clients c ON a.client_id = c.id
                INNER JOIN personas p ON c.id = p.id
            WHERE m.date BETWEEN :startDate AND :endDate AND c.id = :clientId
            ORDER BY m.id DESC
            """,
            nativeQuery = true)
    List<MovementReportProjection> findAllInRangeNative(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("clientId") Long clientId
    );
}
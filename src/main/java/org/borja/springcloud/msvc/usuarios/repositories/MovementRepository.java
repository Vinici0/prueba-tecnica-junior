package org.borja.springcloud.msvc.usuarios.repositories;

import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountAccountNumber(String accountNumber);
}
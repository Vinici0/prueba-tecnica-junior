package org.borja.springcloud.msvc.usuarios.repositories;

import org.borja.springcloud.msvc.usuarios.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);
    Optional<Client> findByClientIdAndStatus(Long clientId, Boolean status);
}
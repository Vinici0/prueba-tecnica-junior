package org.borja.springcloud.msvc.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.borja.springcloud.msvc.usuarios.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByStatus(Boolean status);
    List<Client> findByIdentification(String identification);
    Optional<Client> findByIdAndStatus(Long clientId, Boolean status);
}
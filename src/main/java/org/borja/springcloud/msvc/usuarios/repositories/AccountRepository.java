package org.borja.springcloud.msvc.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.borja.springcloud.msvc.usuarios.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAccountsByStatus(Boolean status);

}
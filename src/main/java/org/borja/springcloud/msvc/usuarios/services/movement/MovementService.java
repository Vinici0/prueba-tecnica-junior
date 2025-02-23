package org.borja.springcloud.msvc.usuarios.services.movement;

import lombok.RequiredArgsConstructor;
import org.borja.springcloud.msvc.usuarios.dto.movement.ReportDto;
import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientBalanceException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Account;
import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.repositories.AccountRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementService implements IMovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    @Override
    public Movement addMovement(Movement movRequest) {
        String accountNumber = movRequest.getAccount().getAccountNumber();
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con número: " + accountNumber));

        double newBalance = account.getInitialBalance() + movRequest.getAmount();

        if (newBalance < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }

        account.setInitialBalance(newBalance);
        accountRepository.save(account);

        movRequest.setDate(LocalDate.now());
        movRequest.setBalance(newBalance);
        movRequest.setAccount(account);

        return movementRepository.save(movRequest);
    }

    @Override
    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }

    @Override
    public Movement getMovementById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movement not found"));
    }

    @Override
    public Movement updateMovement(Long id, Movement data) {
        Movement existingMovement = getMovementById(id);
        existingMovement.setMovementType(data.getMovementType());
        existingMovement.setAmount(data.getAmount());

        return movementRepository.save(existingMovement);
    }

    @Override
    public void deleteMovement(Long id) {
        Movement movement = getMovementById(id);
        movementRepository.delete(movement);
    }

    @Override
    public ReportDto generateReport(Long clientId, LocalDate startDate, LocalDate endDate) {
        List<Account> clientAccounts = accountRepository.findAll()
                .stream()
                .filter(a -> a.getClient().getId().equals(clientId))
                .collect(Collectors.toList());

        List<ReportDto.AccountDto> accountDtoList = clientAccounts.stream().map(a -> {
            List<Movement> movementsInRange = movementRepository.findAll()
                    .stream()
                    .filter(m -> m.getAccount().getAccountNumber().equals(a.getAccountNumber()))
                    .filter(m -> !m.getDate().isBefore(startDate) && !m.getDate().isAfter(endDate))
                    .collect(Collectors.toList());

            List<ReportDto.MovementDto> movementDtoList = movementsInRange.stream().map(mov ->
                    ReportDto.MovementDto.builder()
                            .date(mov.getDate())
                            .movementType(mov.getMovementType())
                            .value(mov.getAmount())
                            .balance(mov.getBalance())
                            .build()
            ).collect(Collectors.toList());

            return ReportDto.AccountDto.builder()
                    .accountNumber(a.getAccountNumber())
                    .currentBalance(a.getInitialBalance())
                    .movements(movementDtoList)
                    .build();
        }).collect(Collectors.toList());

        return ReportDto.builder()
                .clientId(clientId)
                .startDate(startDate)
                .endDate(endDate)
                .accounts(accountDtoList)
                .build();
    }
}
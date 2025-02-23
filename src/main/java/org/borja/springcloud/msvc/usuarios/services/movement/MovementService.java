package org.borja.springcloud.msvc.usuarios.services.movement;

// Java core imports
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// Application imports
import org.borja.springcloud.msvc.usuarios.dto.movement.MovementRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.movement.MovementResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientBalanceException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Account;
import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.repositories.AccountRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovementRepository;
import org.borja.springcloud.msvc.usuarios.repositories.interfaces.MovementReportProjection;
import org.borja.springcloud.msvc.usuarios.utils.validadors.MovementValidator;

@Service
@RequiredArgsConstructor
public class MovementService implements IMovementService {

    private static final Logger log = LoggerFactory.getLogger(MovementService.class);
    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final MovementValidator movementValidator;

    @Override
    public MovementResponseDto addMovement(MovementRequestDto movRequest) {
        log.info("Adding new movement for account: {}", movRequest.getAccountNumber());
        Account account = findAccountByNumber(movRequest.getAccountNumber());
        double initialBalance = account.getInitialBalance();
        validateAndUpdateBalance(account, movRequest.getAmount());
        Movement movement = createNewMovement(account, movRequest, initialBalance);
        Movement savedMovement = movementRepository.save(movement);
        log.info("Movement added successfully with ID: {}", savedMovement.getId());
        return mapToResponseDto(savedMovement);
    }

    @Override
    public List<MovementResponseDto> getAllMovements() {
        log.info("Fetching all movements");
        return movementRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovementResponseDto getMovementById(Long id) {
        log.info("Fetching movement with ID: {}", id);
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movement not found with ID: {}", id);
                    return new ResourceNotFoundException("Movimiento no encontrado con ID: " + id);
                });
        return mapToResponseDto(movement);
    }

    @Override
    public MovementResponseDto updateMovement(Long id, MovementRequestDto movRequest) {
        log.info("Updating movement with ID: {}", id);
        Movement existingMovement = movementRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movement not found with ID: {}", id);
                    return new ResourceNotFoundException("Movimiento no encontrado con ID: " + id);
                });
        Account account = existingMovement.getAccount();
        double currentBalance = account.getInitialBalance();
        double balanceAfterReversal = currentBalance - existingMovement.getAmount();
        double newBalanceAfterUpdate = balanceAfterReversal + movRequest.getAmount();
        if (newBalanceAfterUpdate < 0) {
            log.error("Insufficient balance for movement update. Account: {}", account.getAccountNumber());
            throw new InsufficientBalanceException("Saldo no disponible");
        }
        account.setInitialBalance(newBalanceAfterUpdate);
        accountRepository.save(account);
        existingMovement.setMovementType(movRequest.getMovementType());
        existingMovement.setAmount(movRequest.getAmount());
        existingMovement.setDate(LocalDate.now());
        existingMovement.setBalance(balanceAfterReversal);
        Movement updatedMovement = movementRepository.save(existingMovement);
        log.info("Movement updated successfully with ID: {}", updatedMovement.getId());
        return mapToResponseDto(updatedMovement);
    }

    @Override
    public void deleteMovement(Long id) {
        log.info("Deleting movement with ID: {}", id);
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Movement not found with ID: {}", id);
                    return new ResourceNotFoundException("Movimiento no encontrado con ID: " + id);
                });
        movementRepository.delete(movement);
        log.info("Movement deleted successfully with ID: {}", id);
    }

    @Override
    public List<MovementReportProjection> getCustomReport(LocalDate from, LocalDate to, Long clientId) {
        log.info("Generating movement report for client ID: {} from {} to {}", clientId, from, to);
        return movementRepository.findAllInRangeNative(from, to, clientId);
    }

    private MovementResponseDto mapToResponseDto(Movement movement) {
        return MovementResponseDto.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .movementType(movement.getMovementType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .accountNumber(movement.getAccount().getAccountNumber())
                .build();
    }

    private Account findAccountByNumber(String accountNumber) {
        log.info("Finding account by number: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found: {}", accountNumber);
                    return new ResourceNotFoundException("Cuenta no encontrada: " + accountNumber);
                });
    }

    private void validateAndUpdateBalance(Account account, double amount) {
        double newBalance = account.getInitialBalance() + amount;
        movementValidator.validateBalance(newBalance);
        account.setInitialBalance(newBalance);
        accountRepository.save(account);
    }

    private Movement createNewMovement(Account account, MovementRequestDto request, double initialBalance) {
        return Movement.builder()
                .account(account)
                .date(LocalDate.now())
                .movementType(request.getMovementType())
                .amount(request.getAmount())
                .balance(initialBalance)
                .build();
    }
}

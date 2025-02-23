package org.borja.springcloud.msvc.usuarios.services.movement;

import lombok.RequiredArgsConstructor;

import org.borja.springcloud.msvc.usuarios.dto.movement.MovementRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.movement.MovementResponseDto;
import org.borja.springcloud.msvc.usuarios.dto.report.AccountReportDto;
import org.borja.springcloud.msvc.usuarios.dto.report.MovementReportDetailDto;
import org.borja.springcloud.msvc.usuarios.dto.report.ReportResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientBalanceException;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Account;
import org.borja.springcloud.msvc.usuarios.models.Movement;
import org.borja.springcloud.msvc.usuarios.repositories.AccountRepository;
import org.borja.springcloud.msvc.usuarios.repositories.MovementRepository;
import org.borja.springcloud.msvc.usuarios.repositories.interfaces.MovementReportProjection;
import org.borja.springcloud.msvc.usuarios.utils.validadors.MovementValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementService implements IMovementService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final MovementValidator movementValidator;  

    @Override
    public MovementResponseDto addMovement(MovementRequestDto movRequest) {
        Account account = findAccountByNumber(movRequest.getAccountNumber());
        double initialBalance = account.getInitialBalance();
        validateAndUpdateBalance(account, movRequest.getAmount());
        Movement movement = createNewMovement(account, movRequest, initialBalance);
        
        Movement savedMovement = movementRepository.save(movement);
        return mapToResponseDto(savedMovement);
    }

    @Override
    public List<MovementResponseDto> getAllMovements() {
        return movementRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovementResponseDto getMovementById(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        return mapToResponseDto(movement);
    }

    @Override
    public MovementResponseDto updateMovement(Long id, MovementRequestDto movRequest) {
        Movement existingMovement = movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        Account account = existingMovement.getAccount();

        double baseBalance = account.getInitialBalance() - existingMovement.getAmount();
        double newBalance = baseBalance + movRequest.getAmount();
        if (newBalance < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        account.setInitialBalance(newBalance);
        accountRepository.save(account);

        existingMovement.setMovementType(movRequest.getMovementType());
        existingMovement.setAmount(movRequest.getAmount());
        existingMovement.setDate(LocalDate.now());
        existingMovement.setBalance(newBalance);

        Movement updatedMovement = movementRepository.save(existingMovement);
        return mapToResponseDto(updatedMovement);
    }

    @Override
    public void deleteMovement(Long id) {
        Movement movement = movementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id));
        movementRepository.delete(movement);
    }

    @Override
    public List<MovementReportProjection> getCustomReport(LocalDate from, LocalDate to, Long clientId) {
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
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + accountNumber));
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
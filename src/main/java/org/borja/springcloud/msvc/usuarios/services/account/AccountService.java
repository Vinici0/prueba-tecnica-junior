package org.borja.springcloud.msvc.usuarios.services.account;

// Java core imports
import java.util.List;
import java.util.stream.Collectors;

// Jakarta imports
import jakarta.transaction.Transactional;

// Lombok imports
import lombok.RequiredArgsConstructor;

// Spring framework imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// Application imports
import org.borja.springcloud.msvc.usuarios.dto.account.AccountRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.account.AccountResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Account;
import org.borja.springcloud.msvc.usuarios.models.Client;
import org.borja.springcloud.msvc.usuarios.repositories.AccountRepository;
import org.borja.springcloud.msvc.usuarios.repositories.ClientRepository;


@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public AccountResponseDto addAccount(AccountRequestDto accountDto) {
        log.info("Adding new account for client ID: {}", accountDto.getClientId());
        Client client = clientRepository.findByIdAndStatus(accountDto.getClientId(), true)
                .orElseThrow(() -> {
                    log.warn("Client not found with ID: {}", accountDto.getClientId());
                    return new ResourceNotFoundException("Client not found with ID: " + accountDto.getClientId());
                });

        Account account = new Account();
        account.setAccountType(accountDto.getAccountType());
        account.setInitialBalance(accountDto.getInitialBalance());
        account.setClient(client);

        account = accountRepository.save(account);
        log.info("Account created successfully with account number: {}", account.getAccountNumber());
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public List<AccountResponseDto> getAllAccounts() {
        log.info("Fetching all accounts");
        return accountRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponseDto getAccountByNumber(String accountNumber) {
        log.info("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found with number: {}", accountNumber);
                    return new ResourceNotFoundException("Account not found with number: " + accountNumber);
                });
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccount(String accountNumber, AccountRequestDto accountDto) {
        log.info("Updating account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found with number: {}", accountNumber);
                    return new ResourceNotFoundException("Account not found with number: " + accountNumber);
                });

        Client client = clientRepository.findById(accountDto.getClientId())
                .orElseThrow(() -> {
                    log.warn("Client not found with ID: {}", accountDto.getClientId());
                    return new ResourceNotFoundException("Client not found with ID: " + accountDto.getClientId());
                });

        account.setAccountType(accountDto.getAccountType());
        account.setInitialBalance(accountDto.getInitialBalance());
        account.setStatus(accountDto.getStatus());
        account.setClient(client);

        account = accountRepository.save(account);
        log.info("Account updated successfully: {}", account.getAccountNumber());
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        log.info("Deleting account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Account not found with number: {}", accountNumber);
                    return new ResourceNotFoundException("Account not found with number: " + accountNumber);
                });
        accountRepository.delete(account);
        log.info("Account deleted successfully: {}", accountNumber);
    }

    private AccountResponseDto mapToResponseDto(Account account) {
        return AccountResponseDto.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .clientId(account.getClient().getId())
                .build();
    }
}

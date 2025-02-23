package org.borja.springcloud.msvc.usuarios.services.account;

import jakarta.transaction.Transactional;
import org.borja.springcloud.msvc.usuarios.dto.account.AccountRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.account.AccountResponseDto;
import org.borja.springcloud.msvc.usuarios.exceptions.ResourceNotFoundException;
import org.borja.springcloud.msvc.usuarios.models.Client;
import org.borja.springcloud.msvc.usuarios.models.Account;
import org.borja.springcloud.msvc.usuarios.repositories.AccountRepository;
import org.borja.springcloud.msvc.usuarios.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public AccountResponseDto addAccount(AccountRequestDto accountDto) {
        Client client = clientRepository.findById(accountDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + accountDto.getClientId()));

        System.out.println("Client: " + client);
        Account account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setInitialBalance(accountDto.getInitialBalance());
        account.setStatus(accountDto.getStatus());
        account.setClient(client);

        account = accountRepository.save(account);
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponseDto getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccount(String accountNumber, AccountRequestDto accountDto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));

        Client client = clientRepository.findById(accountDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + accountDto.getClientId()));

        account.setAccountType(accountDto.getAccountType());
        account.setInitialBalance(accountDto.getInitialBalance());
        account.setStatus(accountDto.getStatus());
        account.setClient(client);

        account = accountRepository.save(account);
        return mapToResponseDto(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        accountRepository.delete(account);
    }

    private AccountResponseDto mapToResponseDto(Account account) {
        return AccountResponseDto.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .clientId(account.getClient().getId())
                .build();
    }
}
package org.borja.springcloud.msvc.usuarios.services.account;

import org.borja.springcloud.msvc.usuarios.dto.account.AccountRequestDto;
import org.borja.springcloud.msvc.usuarios.dto.account.AccountResponseDto;

import java.util.List;

public interface IAccountService {
    AccountResponseDto addAccount(AccountRequestDto accountDto);

    List<AccountResponseDto> getAllAccounts();

    AccountResponseDto getAccountByNumber(String accountNumber);

    AccountResponseDto updateAccount(String accountNumber, AccountRequestDto accountDto);

    void deleteAccount(String accountNumber);
}

package org.borja.springcloud.msvc.usuarios.dto.account;

import lombok.Builder;
import lombok.Data;
import org.borja.springcloud.msvc.usuarios.models.enums.AccountType;

@Data
@Builder
public class AccountResponseDto {
    private String accountNumber;
    private AccountType accountType;
    private Double initialBalance;
    private Boolean status;
    private Long clientId;
}
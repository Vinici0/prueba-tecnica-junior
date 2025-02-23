package org.borja.springcloud.msvc.usuarios.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.borja.springcloud.msvc.usuarios.models.enums.AccountType;

@Data
@Builder
public class AccountRequestDto {

    @NotNull
    private AccountType accountType;

    @NotNull
    private Double initialBalance;

    private Boolean status;

    @Positive
    private Long clientId;
}
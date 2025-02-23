package org.borja.springcloud.msvc.usuarios.utils.validadors;

import lombok.RequiredArgsConstructor;
import org.borja.springcloud.msvc.usuarios.exceptions.InsufficientBalanceException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovementValidator {

    public void validateBalance(double newBalance) {
        if (newBalance < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }
    }
}
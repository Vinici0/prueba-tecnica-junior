package org.borja.springcloud.msvc.usuarios.exceptions;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
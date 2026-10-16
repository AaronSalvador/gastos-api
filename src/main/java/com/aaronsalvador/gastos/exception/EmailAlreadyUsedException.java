package com.aaronsalvador.gastos.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("Ya existe una cuenta con el correo " + email);
    }
}

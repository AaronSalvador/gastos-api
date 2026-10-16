package com.aaronsalvador.gastos.exception;

/** El usuario hizo demasiadas peticiones en poco tiempo. */
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}

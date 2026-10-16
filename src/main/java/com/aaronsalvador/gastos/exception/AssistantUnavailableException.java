package com.aaronsalvador.gastos.exception;

/** El asistente de IA no está configurado o el proveedor no respondió. */
public class AssistantUnavailableException extends RuntimeException {
    public AssistantUnavailableException(String message) {
        super(message);
    }
}

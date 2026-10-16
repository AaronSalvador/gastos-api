package com.aaronsalvador.gastos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Pregunta al asistente, con el historial reciente y un resumen de los gastos del usuario. */
public record AssistantRequest(
        @NotBlank(message = "Escribe un mensaje")
        @Size(max = 500, message = "El mensaje es demasiado largo (máximo 500 caracteres)")
        String message,

        @Size(max = 20, message = "El historial es demasiado largo")
        List<@Valid Turn> history,

        @Valid Context context) {

    public record Turn(
            @NotBlank @Pattern(regexp = "user|assistant", message = "Rol no válido") String role,
            @NotBlank @Size(max = 2000) String content) {
    }

    public record Context(
            BigDecimal total,
            @Size(max = 12) Map<String, BigDecimal> byCategory,
            @Size(max = 10) List<@Valid Item> recent) {
    }

    public record Item(
            @Size(max = 20) String date,
            @Size(max = 120) String description,
            BigDecimal amount,
            @Size(max = 40) String category) {
    }
}

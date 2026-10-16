package com.aaronsalvador.gastos.dto;

import com.aaronsalvador.gastos.model.Category;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 120, message = "Máximo 120 caracteres") String description,
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0") BigDecimal amount,
        @NotNull(message = "La categoría es obligatoria") Category category,
        @NotNull(message = "La fecha es obligatoria")
        @PastOrPresent(message = "La fecha no puede ser futura") LocalDate date) {
}

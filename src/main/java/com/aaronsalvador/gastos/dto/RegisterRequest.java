package com.aaronsalvador.gastos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio") String fullName,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Correo inválido") String email,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password) {
}

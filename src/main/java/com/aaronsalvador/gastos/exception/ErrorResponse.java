package com.aaronsalvador.gastos.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(Instant timestamp, int status, String error, Map<String, String> details) {
    public static ErrorResponse of(int status, String error) {
        return new ErrorResponse(Instant.now(), status, error, null);
    }
}

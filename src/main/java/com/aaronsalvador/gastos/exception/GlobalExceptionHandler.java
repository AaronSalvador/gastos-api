package com.aaronsalvador.gastos.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(f -> details.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(Instant.now(), 400, "Datos inválidos", details));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(400, "Solicitud mal formada o con valores no permitidos"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(404, ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleConflict(EmailAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(409, ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(401, "Correo o contraseña incorrectos"));
    }

    @ExceptionHandler(AssistantUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleAssistantUnavailable(AssistantUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse.of(503, ex.getMessage()));
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ErrorResponse> handleTooManyRequests(TooManyRequestsException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ErrorResponse.of(429, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(500, "Error interno del servidor"));
    }
}

package com.aaronsalvador.gastos.controller;

import com.aaronsalvador.gastos.dto.*;
import com.aaronsalvador.gastos.model.Category;
import com.aaronsalvador.gastos.model.User;
import com.aaronsalvador.gastos.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Gastos")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar un gasto")
    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user, request));
    }

    @Operation(summary = "Listar mis gastos (paginado, filtro opcional por categoría)")
    @GetMapping
    public PageResponse<ExpenseResponse> list(@AuthenticationPrincipal User user,
                                              @RequestParam(required = false) Category category,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return service.list(user, category, page, size);
    }

    @Operation(summary = "Resumen: total gastado y total por categoría")
    @GetMapping("/summary")
    public SummaryResponse summary(@AuthenticationPrincipal User user) {
        return service.summary(user);
    }

    @Operation(summary = "Obtener un gasto por id")
    @GetMapping("/{id}")
    public ExpenseResponse get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return service.get(user, id);
    }

    @Operation(summary = "Actualizar un gasto")
    @PutMapping("/{id}")
    public ExpenseResponse update(@AuthenticationPrincipal User user, @PathVariable Long id,
                                  @Valid @RequestBody ExpenseRequest request) {
        return service.update(user, id, request);
    }

    @Operation(summary = "Eliminar un gasto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        service.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}

package com.aaronsalvador.gastos.service;

import com.aaronsalvador.gastos.dto.ExpenseRequest;
import com.aaronsalvador.gastos.dto.ExpenseResponse;
import com.aaronsalvador.gastos.exception.ResourceNotFoundException;
import com.aaronsalvador.gastos.model.Category;
import com.aaronsalvador.gastos.model.Expense;
import com.aaronsalvador.gastos.model.User;
import com.aaronsalvador.gastos.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock ExpenseRepository repository;
    @InjectMocks ExpenseService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("aaron@test.com");
    }

    @Test
    void create_guardaElGastoConLosDatosRecibidos() {
        when(repository.save(any(Expense.class))).thenAnswer(inv -> inv.getArgument(0));
        ExpenseRequest request = new ExpenseRequest("  Almuerzo ", new BigDecimal("15.50"), Category.COMIDA, LocalDate.now());

        ExpenseResponse response = service.create(user, request);

        assertEquals("Almuerzo", response.description());
        assertEquals(new BigDecimal("15.50"), response.amount());
        assertEquals(Category.COMIDA, response.category());
    }

    @Test
    void get_lanzaNotFoundSiElGastoNoEsDelUsuario() {
        when(repository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(user, 99L));
    }

    @Test
    void delete_eliminaElGastoDelUsuario() {
        Expense expense = new Expense();
        when(repository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(expense));

        service.delete(user, 5L);

        verify(repository).delete(expense);
    }

    @Test
    void update_lanzaNotFoundSiNoExiste() {
        when(repository.findByIdAndUserId(7L, 1L)).thenReturn(Optional.empty());
        ExpenseRequest request = new ExpenseRequest("Bus", new BigDecimal("2.00"), Category.TRANSPORTE, LocalDate.now());

        assertThrows(ResourceNotFoundException.class, () -> service.update(user, 7L, request));
        verify(repository, never()).save(any());
    }
}

package com.aaronsalvador.gastos.dto;

import com.aaronsalvador.gastos.model.Category;
import com.aaronsalvador.gastos.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Long id, String description, BigDecimal amount, Category category, LocalDate date) {
    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(e.getId(), e.getDescription(), e.getAmount(), e.getCategory(), e.getDate());
    }
}

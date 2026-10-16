package com.aaronsalvador.gastos.service;

import com.aaronsalvador.gastos.dto.*;
import com.aaronsalvador.gastos.exception.ResourceNotFoundException;
import com.aaronsalvador.gastos.model.Category;
import com.aaronsalvador.gastos.model.Expense;
import com.aaronsalvador.gastos.model.User;
import com.aaronsalvador.gastos.repository.CategoryTotal;
import com.aaronsalvador.gastos.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ExpenseResponse create(User user, ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setUser(user);
        apply(expense, request);
        return ExpenseResponse.from(repository.save(expense));
    }

    @Transactional(readOnly = true)
    public PageResponse<ExpenseResponse> list(User user, Category category, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100),
                Sort.by(Sort.Direction.DESC, "date"));
        Page<Expense> result = (category == null)
                ? repository.findByUserId(user.getId(), pageable)
                : repository.findByUserIdAndCategory(user.getId(), category, pageable);
        return PageResponse.from(result.map(ExpenseResponse::from));
    }

    @Transactional(readOnly = true)
    public ExpenseResponse get(User user, Long id) {
        return ExpenseResponse.from(findOwned(user, id));
    }

    @Transactional
    public ExpenseResponse update(User user, Long id, ExpenseRequest request) {
        Expense expense = findOwned(user, id);
        apply(expense, request);
        return ExpenseResponse.from(repository.save(expense));
    }

    @Transactional
    public void delete(User user, Long id) {
        repository.delete(findOwned(user, id));
    }

    @Transactional(readOnly = true)
    public SummaryResponse summary(User user) {
        List<CategoryTotal> rows = repository.totalsByCategory(user.getId());
        Map<Category, BigDecimal> byCategory = new EnumMap<>(Category.class);
        BigDecimal total = BigDecimal.ZERO;
        for (CategoryTotal row : rows) {
            byCategory.put(row.getCategory(), row.getTotal());
            total = total.add(row.getTotal());
        }
        return new SummaryResponse(total, byCategory);
    }

    // Solo devuelve gastos del usuario: si es de otro, responde 404 (no revela que existe).
    private Expense findOwned(User user, Long id) {
        return repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado con id " + id));
    }

    private void apply(Expense expense, ExpenseRequest request) {
        expense.setDescription(request.description().trim());
        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setDate(request.date());
    }
}

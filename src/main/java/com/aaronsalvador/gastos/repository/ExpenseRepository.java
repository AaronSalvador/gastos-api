package com.aaronsalvador.gastos.repository;

import com.aaronsalvador.gastos.model.Category;
import com.aaronsalvador.gastos.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    Page<Expense> findByUserIdAndCategory(Long userId, Category category, Pageable pageable);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    @Query("select e.category as category, sum(e.amount) as total " +
           "from Expense e where e.user.id = :userId group by e.category")
    List<CategoryTotal> totalsByCategory(@Param("userId") Long userId);
}

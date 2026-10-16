package com.aaronsalvador.gastos.repository;

import com.aaronsalvador.gastos.model.Category;

import java.math.BigDecimal;

public interface CategoryTotal {
    Category getCategory();
    BigDecimal getTotal();
}

package com.aaronsalvador.gastos.dto;

import com.aaronsalvador.gastos.model.Category;

import java.math.BigDecimal;
import java.util.Map;

public record SummaryResponse(BigDecimal total, Map<Category, BigDecimal> byCategory) {
}

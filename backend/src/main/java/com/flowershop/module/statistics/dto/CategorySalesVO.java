package com.flowershop.module.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySalesVO {
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private Long orderCount;
}

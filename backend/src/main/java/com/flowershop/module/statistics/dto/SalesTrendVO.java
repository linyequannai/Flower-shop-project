package com.flowershop.module.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrendVO {
    private String date;
    private BigDecimal amount;
    private Long orderCount;
}

package com.flowershop.module.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardVO {
    private BigDecimal totalRevenue;
    private Long orderCount;
    private Long userCount;
    private Long flowerCount;
    private Long todayOrderCount;
    private BigDecimal todayRevenue;
}

package com.flowershop.module.statistics.service;

import com.flowershop.module.statistics.dto.CategorySalesVO;
import com.flowershop.module.statistics.dto.DashboardVO;
import com.flowershop.module.statistics.dto.SalesTrendVO;

import java.util.List;

public interface StatisticsService {
    DashboardVO getDashboard();
    List<SalesTrendVO> getSalesTrend(String period);
    List<CategorySalesVO> getCategorySales();
    List<SalesTrendVO> getTopFlowers(int limit);
}

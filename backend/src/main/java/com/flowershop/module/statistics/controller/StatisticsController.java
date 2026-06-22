package com.flowershop.module.statistics.controller;

import com.flowershop.common.Result;
import com.flowershop.module.statistics.dto.CategorySalesVO;
import com.flowershop.module.statistics.dto.DashboardVO;
import com.flowershop.module.statistics.dto.SalesTrendVO;
import com.flowershop.module.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public Result<DashboardVO> dashboard() {
        return Result.ok(statisticsService.getDashboard());
    }

    @GetMapping("/sales-trend")
    public Result<List<SalesTrendVO>> salesTrend(@RequestParam(defaultValue = "month") String period) {
        return Result.ok(statisticsService.getSalesTrend(period));
    }

    @GetMapping("/category-sales")
    public Result<List<CategorySalesVO>> categorySales() {
        return Result.ok(statisticsService.getCategorySales());
    }

    @GetMapping("/top-flowers")
    public Result<List<SalesTrendVO>> topFlowers(@RequestParam(defaultValue = "10") int limit) {
        return Result.ok(statisticsService.getTopFlowers(limit));
    }
}

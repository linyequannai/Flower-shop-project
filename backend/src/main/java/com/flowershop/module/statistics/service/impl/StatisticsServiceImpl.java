package com.flowershop.module.statistics.service.impl;

import com.flowershop.module.statistics.dto.CategorySalesVO;
import com.flowershop.module.statistics.dto.DashboardVO;
import com.flowershop.module.statistics.dto.SalesTrendVO;
import com.flowershop.module.statistics.mapper.StatisticsMapper;
import com.flowershop.module.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    @Override
    public DashboardVO getDashboard() {
        return statisticsMapper.selectDashboard();
    }

    @Override
    public List<SalesTrendVO> getSalesTrend(String period) {
        String groupFormat;
        int limit;
        switch (period) {
            case "year":
                groupFormat = "%Y-%m";
                limit = 12;
                break;
            case "week":
                groupFormat = "%Y-%m-%d";
                limit = 7;
                break;
            default: // month
                groupFormat = "%Y-%m-%d";
                limit = 30;
                break;
        }
        return statisticsMapper.selectSalesTrend(groupFormat, limit);
    }

    @Override
    public List<CategorySalesVO> getCategorySales() {
        return statisticsMapper.selectCategorySales();
    }

    @Override
    public List<SalesTrendVO> getTopFlowers(int limit) {
        return statisticsMapper.selectTopFlowers(limit);
    }
}

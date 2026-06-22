package com.flowershop.module.statistics.mapper;

import com.flowershop.module.statistics.dto.CategorySalesVO;
import com.flowershop.module.statistics.dto.DashboardVO;
import com.flowershop.module.statistics.dto.SalesTrendVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatisticsMapper {

    DashboardVO selectDashboard();

    List<SalesTrendVO> selectSalesTrend(@Param("groupFormat") String groupFormat,
                                        @Param("limit") int limit);

    List<CategorySalesVO> selectCategorySales();

    List<SalesTrendVO> selectTopFlowers(@Param("limit") int limit);
}

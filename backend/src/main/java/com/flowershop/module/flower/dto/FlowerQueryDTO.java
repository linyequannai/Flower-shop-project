package com.flowershop.module.flower.dto;

import lombok.Data;

@Data
public class FlowerQueryDTO {
    private String keyword;
    private Long categoryId;
    private String sort;        // sales=销量, price_asc=价格升, price_desc=价格降, default=默认
    private Integer page = 1;
    private Integer size = 12;
}

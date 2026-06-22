package com.flowershop.module.flower.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlowerVO {
    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sales;
    private String description;
    private String coverImage;
    private String images;
    private Integer status;
    private LocalDateTime createdAt;
}

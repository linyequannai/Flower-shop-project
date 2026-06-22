package com.flowershop.module.favorite.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FavoriteVO {
    private Long id;
    private Long flowerId;
    private String flowerName;
    private String flowerImage;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer sales;
    private LocalDateTime createdAt;
}

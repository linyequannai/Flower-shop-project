package com.flowershop.module.cart.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemVO {
    private Long id;
    private Long flowerId;
    private String flowerName;
    private String flowerImage;
    private BigDecimal price;
    private Integer quantity;
    private Integer selected;
    private Integer stock;      // 当前库存
}

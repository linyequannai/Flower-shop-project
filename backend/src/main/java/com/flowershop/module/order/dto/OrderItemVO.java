package com.flowershop.module.order.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemVO {
    private Long id;
    private Long flowerId;
    private String flowerName;
    private String flowerImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}

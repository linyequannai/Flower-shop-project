package com.flowershop.module.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("order_item")
public class OrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long flowerId;
    private String flowerName;
    private String flowerImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}

package com.flowershop.module.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cart_item")
public class CartItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long flowerId;
    private Integer quantity;
    private Integer selected;   // 0=未选中, 1=选中

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}

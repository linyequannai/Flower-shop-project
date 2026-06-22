package com.flowershop.module.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDTO {
    @NotNull(message = "花卉ID不能为空")
    private Long flowerId;

    @NotNull(message = "数量不能为空")
    private Integer quantity;
}

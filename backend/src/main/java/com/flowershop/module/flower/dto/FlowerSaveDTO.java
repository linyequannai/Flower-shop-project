package com.flowershop.module.flower.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FlowerSaveDTO {
    @NotBlank(message = "花卉名称不能为空")
    private String name;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;

    @NotNull(message = "库存不能为空")
    private Integer stock;

    private String description;
    private String coverImage;
    private String images;
    private Integer status;
}

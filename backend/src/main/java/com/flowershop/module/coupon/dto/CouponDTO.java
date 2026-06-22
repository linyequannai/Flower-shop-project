package com.flowershop.module.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDTO {
    @NotBlank(message = "优惠券名称不能为空")
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    private Integer type;

    @NotNull(message = "优惠券面值不能为空")
    private BigDecimal value;

    private BigDecimal minAmount;

    @NotNull(message = "发放数量不能为空")
    private Integer totalCount;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    private Integer status;
}

package com.flowershop.module.coupon.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponVO {
    private Long id;
    private String name;
    private Integer type;
    private String typeText;    // "满减券" / "折扣券"
    private BigDecimal value;
    private BigDecimal minAmount;
    private Integer totalCount;
    private Integer usedCount;
    private Integer remainCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createdAt;
    // 用户领券相关
    private Long userCouponId;
    private Integer userCouponStatus;
    private LocalDateTime usedAt;
}

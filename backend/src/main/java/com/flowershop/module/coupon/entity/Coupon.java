package com.flowershop.module.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer type;       // 0=满减, 1=折扣
    private BigDecimal value;   // 满减金额(元) 或 折扣率(0.90=9折)
    private BigDecimal minAmount;
    private Integer totalCount;
    private Integer usedCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;     // 0=禁用, 1=启用

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}

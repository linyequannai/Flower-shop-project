package com.flowershop.module.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String statusText;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private Long couponId;
    private String couponName;
    private List<OrderItemVO> items;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}

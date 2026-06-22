package com.flowershop.module.review.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long flowerId;
    private Long orderId;
    private Integer rating;     // 1-5
    private String content;
    private String images;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}

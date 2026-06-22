package com.flowershop.module.review.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewVO {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long flowerId;
    private Long orderId;
    private Integer rating;
    private String content;
    private String images;
    private LocalDateTime createdAt;
}

package com.flowershop.module.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer role;
    private Integer status;
    private LocalDateTime createdAt;
}

package com.flowershop.module.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nickname;
    private String email;

    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private String avatar;
}

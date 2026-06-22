package com.flowershop.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flowershop.module.user.dto.*;
import com.flowershop.module.user.entity.User;

public interface UserService extends IService<User> {
    UserVO register(RegisterDTO dto);
    String login(LoginDTO dto);
    UserVO getCurrentUser(Long userId);
    void updateProfile(Long userId, UserUpdateDTO dto);
    void updatePassword(Long userId, PasswordUpdateDTO dto);
}

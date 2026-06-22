package com.flowershop.module.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.PageResult;
import com.flowershop.common.Result;
import com.flowershop.module.user.dto.PasswordUpdateDTO;
import com.flowershop.module.user.dto.UserUpdateDTO;
import com.flowershop.module.user.dto.UserVO;
import com.flowershop.module.user.entity.User;
import com.flowershop.module.user.service.UserService;
import com.flowershop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/user/profile")
    public Result<Void> updateProfile(@CurrentUser Long userId, @RequestBody UserUpdateDTO dto) {
        userService.updateProfile(userId, dto);
        return Result.ok();
    }

    @PutMapping("/user/password")
    public Result<Void> updatePassword(@CurrentUser Long userId, @RequestBody PasswordUpdateDTO dto) {
        userService.updatePassword(userId, dto);
        return Result.ok();
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<UserVO>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userService.page(new Page<>(page, size));
        List<UserVO> voList = userPage.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(u, vo);
            return vo;
        }).toList();
        return Result.ok(PageResult.of(userPage.getTotal(), page, size, voList));
    }

    @PutMapping("/admin/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user != null) {
            user.setStatus(status);
            userService.updateById(user);
        }
        return Result.ok();
    }
}

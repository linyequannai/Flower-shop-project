package com.flowershop.module.user.controller;

import com.flowershop.common.Result;
import com.flowershop.module.user.dto.LoginDTO;
import com.flowershop.module.user.dto.RegisterDTO;
import com.flowershop.module.user.dto.UserVO;
import com.flowershop.module.user.service.UserService;
import com.flowershop.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        return Result.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public Result<UserVO> me(@CurrentUser Long userId) {
        return Result.ok(userService.getCurrentUser(userId));
    }
}

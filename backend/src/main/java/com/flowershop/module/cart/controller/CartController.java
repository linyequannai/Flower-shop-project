package com.flowershop.module.cart.controller;

import com.flowershop.common.Result;
import com.flowershop.module.cart.dto.CartItemDTO;
import com.flowershop.module.cart.dto.CartItemVO;
import com.flowershop.module.cart.service.CartService;
import com.flowershop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Result<List<CartItemVO>> list(@CurrentUser Long userId) {
        return Result.ok(cartService.list(userId));
    }

    @PostMapping
    public Result<Void> add(@CurrentUser Long userId, @RequestBody CartItemDTO dto) {
        cartService.add(userId, dto);
        return Result.ok();
    }

    @PutMapping("/{id}/quantity")
    public Result<Void> updateQuantity(@CurrentUser Long userId, @PathVariable Long id,
                                       @RequestBody Map<String, Integer> body) {
        cartService.updateQuantity(userId, id, body.get("quantity"));
        return Result.ok();
    }

    @PutMapping("/{id}/select")
    public Result<Void> updateSelected(@CurrentUser Long userId, @PathVariable Long id,
                                       @RequestBody Map<String, Integer> body) {
        cartService.updateSelected(userId, id, body.get("selected"));
        return Result.ok();
    }

    @PutMapping("/select-all")
    public Result<Void> selectAll(@CurrentUser Long userId, @RequestBody Map<String, Integer> body) {
        cartService.selectAll(userId, body.get("selected"));
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@CurrentUser Long userId, @PathVariable Long id) {
        cartService.remove(userId, id);
        return Result.ok();
    }

    @DeleteMapping
    public Result<Void> clearSelected(@CurrentUser Long userId) {
        cartService.clearSelected(userId);
        return Result.ok();
    }
}

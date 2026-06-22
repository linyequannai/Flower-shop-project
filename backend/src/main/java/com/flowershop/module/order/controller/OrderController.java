package com.flowershop.module.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.Result;
import com.flowershop.module.order.dto.OrderCreateDTO;
import com.flowershop.module.order.dto.OrderVO;
import com.flowershop.module.order.service.OrderService;
import com.flowershop.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public Result<OrderVO> create(@CurrentUser Long userId, @Valid @RequestBody OrderCreateDTO dto) {
        return Result.ok(orderService.create(userId, dto));
    }

    @GetMapping("/orders")
    public Result<Map<String, Object>> listByUser(@CurrentUser Long userId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(required = false) Integer status) {
        Page<OrderVO> result = orderService.pageByUser(userId, page, size, status);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        map.put("size", result.getSize());
        map.put("records", result.getRecords());
        return Result.ok(map);
    }

    @GetMapping("/orders/{id}")
    public Result<OrderVO> detail(@CurrentUser Long userId, @PathVariable Long id) {
        return Result.ok(orderService.getDetail(id));
    }

    @PutMapping("/orders/{id}/cancel")
    public Result<Void> cancel(@CurrentUser Long userId, @PathVariable Long id) {
        orderService.cancel(userId, id);
        return Result.ok();
    }

    @PutMapping("/orders/{id}/confirm")
    public Result<Void> confirm(@CurrentUser Long userId, @PathVariable Long id) {
        orderService.confirm(userId, id);
        return Result.ok();
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> listAll(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(required = false) Integer status) {
        Page<OrderVO> result = orderService.pageAll(page, size, status);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        map.put("size", result.getSize());
        map.put("records", result.getRecords());
        return Result.ok(map);
    }

    @PutMapping("/admin/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        orderService.updateStatus(id, status);
        return Result.ok();
    }

    @PutMapping("/admin/orders/{id}/ship")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> ship(@PathVariable Long id) {
        orderService.ship(id);
        return Result.ok();
    }
}

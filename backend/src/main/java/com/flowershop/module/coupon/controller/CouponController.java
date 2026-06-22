package com.flowershop.module.coupon.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.Result;
import com.flowershop.module.coupon.dto.CouponDTO;
import com.flowershop.module.coupon.dto.CouponVO;
import com.flowershop.module.coupon.service.CouponService;
import com.flowershop.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/coupons/available")
    public Result<List<CouponVO>> available(@CurrentUser Long userId) {
        return Result.ok(couponService.listAvailable(userId));
    }

    @GetMapping("/coupons/mine")
    public Result<List<CouponVO>> mine(@CurrentUser Long userId,
                                        @RequestParam(required = false) Integer status) {
        return Result.ok(couponService.listMine(userId, status));
    }

    @PostMapping("/coupons/{id}/claim")
    public Result<Void> claim(@CurrentUser Long userId, @PathVariable Long id) {
        couponService.claim(userId, id);
        return Result.ok();
    }

    @GetMapping("/admin/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> listAll(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Page<CouponVO> result = couponService.pageAll(page, size);
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("pages", result.getPages());
        map.put("current", result.getCurrent());
        map.put("size", result.getSize());
        map.put("records", result.getRecords());
        return Result.ok(map);
    }

    @PostMapping("/admin/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CouponVO> create(@Valid @RequestBody CouponDTO dto) {
        return Result.ok(couponService.create(dto));
    }

    @PutMapping("/admin/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CouponVO> update(@PathVariable Long id, @Valid @RequestBody CouponDTO dto) {
        return Result.ok(couponService.update(id, dto));
    }

    @DeleteMapping("/admin/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        couponService.delete(id);
        return Result.ok();
    }
}

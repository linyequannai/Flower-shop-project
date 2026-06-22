package com.flowershop.module.coupon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.module.coupon.dto.CouponDTO;
import com.flowershop.module.coupon.dto.CouponVO;

import java.util.List;

public interface CouponService {
    List<CouponVO> listAvailable(Long userId);
    List<CouponVO> listMine(Long userId, Integer status);
    void claim(Long userId, Long couponId);
    // 管理员
    Page<CouponVO> pageAll(int page, int size);
    CouponVO create(CouponDTO dto);
    CouponVO update(Long id, CouponDTO dto);
    void delete(Long id);
}

package com.flowershop.module.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.BusinessException;
import com.flowershop.module.coupon.dto.CouponDTO;
import com.flowershop.module.coupon.dto.CouponVO;
import com.flowershop.module.coupon.entity.Coupon;
import com.flowershop.module.coupon.entity.UserCoupon;
import com.flowershop.module.coupon.mapper.CouponMapper;
import com.flowershop.module.coupon.mapper.UserCouponMapper;
import com.flowershop.module.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    @Override
    public List<CouponVO> listAvailable(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        // 可领取的优惠券：启用的、在有效期内、还有库存的
        List<Coupon> coupons = couponMapper.selectList(
                new LambdaQueryWrapper<Coupon>()
                        .eq(Coupon::getStatus, 1)
                        .le(Coupon::getStartTime, now)
                        .ge(Coupon::getEndTime, now)
        );
        // 过滤掉已领取过的
        return coupons.stream()
                .filter(c -> c.getUsedCount() < c.getTotalCount())
                .map(c -> {
                    CouponVO vo = buildVO(c);
                    vo.setRemainCount(c.getTotalCount() - c.getUsedCount());
                    // 检查用户是否已领取
                    long claimed = userCouponMapper.selectCount(
                            new LambdaQueryWrapper<UserCoupon>()
                                    .eq(UserCoupon::getUserId, userId)
                                    .eq(UserCoupon::getCouponId, c.getId())
                    );
                    vo.setUserCouponStatus(claimed > 0 ? -1 : null); // -1 表示已领取
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponVO> listMine(Long userId, Integer status) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getUserId, userId);
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        List<UserCoupon> userCoupons = userCouponMapper.selectList(wrapper);
        return userCoupons.stream().map(uc -> {
            Coupon coupon = couponMapper.selectById(uc.getCouponId());
            CouponVO vo = buildVO(coupon);
            vo.setUserCouponId(uc.getId());
            vo.setUserCouponStatus(uc.getStatus());
            vo.setUsedAt(uc.getUsedAt());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void claim(Long userId, Long couponId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null || coupon.getStatus() == 0) {
            throw new BusinessException("优惠券不存在或已禁用");
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new BusinessException("不在优惠券领取时间内");
        }
        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            throw new BusinessException("优惠券已被领完");
        }
        // 检查是否已领取
        long claimed = userCouponMapper.selectCount(
                new LambdaQueryWrapper<UserCoupon>()
                        .eq(UserCoupon::getUserId, userId)
                        .eq(UserCoupon::getCouponId, couponId)
        );
        if (claimed > 0) {
            throw new BusinessException("已领取过该优惠券");
        }

        UserCoupon uc = new UserCoupon();
        uc.setUserId(userId);
        uc.setCouponId(couponId);
        uc.setStatus(0);
        userCouponMapper.insert(uc);
    }

    @Override
    public Page<CouponVO> pageAll(int page, int size) {
        Page<Coupon> couponPage = couponMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Coupon>().orderByDesc(Coupon::getCreatedAt));
        Page<CouponVO> voPage = new Page<>(page, size, couponPage.getTotal());
        voPage.setRecords(couponPage.getRecords().stream().map(this::buildVO).toList());
        return voPage;
    }

    @Override
    public CouponVO create(CouponDTO dto) {
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(dto, coupon);
        if (coupon.getStatus() == null) coupon.setStatus(1);
        if (coupon.getUsedCount() == null) coupon.setUsedCount(0);
        couponMapper.insert(coupon);
        return buildVO(coupon);
    }

    @Override
    public CouponVO update(Long id, CouponDTO dto) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        BeanUtils.copyProperties(dto, coupon, "id", "usedCount", "createdAt");
        couponMapper.updateById(coupon);
        return buildVO(coupon);
    }

    @Override
    public void delete(Long id) {
        couponMapper.deleteById(id);
    }

    private CouponVO buildVO(Coupon coupon) {
        if (coupon == null) return null;
        CouponVO vo = new CouponVO();
        BeanUtils.copyProperties(coupon, vo);
        vo.setTypeText(coupon.getType() == 0 ? "满减券" : "折扣券");
        vo.setRemainCount(coupon.getTotalCount() - coupon.getUsedCount());
        return vo;
    }
}

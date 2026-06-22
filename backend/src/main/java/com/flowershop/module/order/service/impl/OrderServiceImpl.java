package com.flowershop.module.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.BusinessException;
import com.flowershop.module.cart.entity.CartItem;
import com.flowershop.module.cart.mapper.CartItemMapper;
import com.flowershop.module.coupon.entity.Coupon;
import com.flowershop.module.coupon.entity.UserCoupon;
import com.flowershop.module.coupon.mapper.CouponMapper;
import com.flowershop.module.coupon.mapper.UserCouponMapper;
import com.flowershop.module.flower.entity.Flower;
import com.flowershop.module.flower.mapper.FlowerMapper;
import com.flowershop.module.order.dto.OrderCreateDTO;
import com.flowershop.module.order.dto.OrderItemVO;
import com.flowershop.module.order.dto.OrderVO;
import com.flowershop.module.order.entity.Order;
import com.flowershop.module.order.entity.OrderItem;
import com.flowershop.module.order.mapper.OrderItemMapper;
import com.flowershop.module.order.mapper.OrderMapper;
import com.flowershop.module.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final FlowerMapper flowerMapper;
    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    private static final Map<Integer, String> STATUS_MAP = Map.of(
            0, "待付款", 1, "已付款", 2, "已发货", 3, "已完成", 4, "已取消", 5, "已退款"
    );

    @Override
    @Transactional
    public OrderVO create(Long userId, OrderCreateDTO dto) {
        // 获取选中的购物车项
        List<CartItem> cartItems = cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId)
                        .eq(CartItem::getSelected, 1)
                        .in(CartItem::getId, dto.getCartItemIds())
        );
        if (cartItems.isEmpty()) {
            throw new BusinessException("购物车中没有选中的商品");
        }

        // 计算总价
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            Flower flower = flowerMapper.selectById(ci.getFlowerId());
            if (flower == null || flower.getStatus() == 0) {
                throw new BusinessException("商品【" + (flower != null ? flower.getName() : "未知") + "】已下架");
            }
            if (flower.getStock() < ci.getQuantity()) {
                throw new BusinessException("商品【" + flower.getName() + "】库存不足");
            }
            totalAmount = totalAmount.add(flower.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }

        // 处理优惠券
        BigDecimal discountAmount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (dto.getCouponId() != null) {
            // 验证优惠券
            UserCoupon userCoupon = userCouponMapper.selectOne(
                    new LambdaQueryWrapper<UserCoupon>()
                            .eq(UserCoupon::getUserId, userId)
                            .eq(UserCoupon::getCouponId, dto.getCouponId())
                            .eq(UserCoupon::getStatus, 0)
            );
            if (userCoupon == null) {
                throw new BusinessException("优惠券不可用");
            }
            coupon = couponMapper.selectById(dto.getCouponId());
            if (coupon == null || coupon.getStatus() == 0) {
                throw new BusinessException("优惠券已失效");
            }
            if (LocalDateTime.now().isAfter(coupon.getEndTime())) {
                throw new BusinessException("优惠券已过期");
            }
            if (totalAmount.compareTo(coupon.getMinAmount()) < 0) {
                throw new BusinessException("未达到优惠券使用门槛");
            }

            // 计算优惠金额
            if (coupon.getType() == 0) {
                // 满减
                discountAmount = coupon.getValue();
            } else {
                // 折扣
                discountAmount = totalAmount.multiply(
                        BigDecimal.ONE.subtract(coupon.getValue())).setScale(2, RoundingMode.HALF_UP);
            }
            if (discountAmount.compareTo(totalAmount) > 0) {
                discountAmount = totalAmount;
            }

            // 标记优惠券已使用
            userCoupon.setStatus(1);
            userCoupon.setUsedAt(LocalDateTime.now());
            userCouponMapper.updateById(userCoupon);

            // 更新优惠券已使用量
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponMapper.updateById(coupon);
        }

        BigDecimal payAmount = totalAmount.subtract(discountAmount);

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setPayAmount(payAmount);
        order.setStatus(0);
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setReceiverAddress(dto.getReceiverAddress());
        order.setRemark(dto.getRemark());
        order.setCouponId(dto.getCouponId());
        orderMapper.insert(order);

        // 创建订单明细 + 扣库存 + 更新销量
        for (CartItem ci : cartItems) {
            Flower flower = flowerMapper.selectById(ci.getFlowerId());

            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setFlowerId(flower.getId());
            oi.setFlowerName(flower.getName());
            oi.setFlowerImage(flower.getCoverImage());
            oi.setPrice(flower.getPrice());
            oi.setQuantity(ci.getQuantity());
            oi.setSubtotal(flower.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            orderItemMapper.insert(oi);

            // 扣库存 + 增加销量
            flower.setStock(flower.getStock() - ci.getQuantity());
            flower.setSales(flower.getSales() + ci.getQuantity());
            flowerMapper.updateById(flower);
        }

        // 清除已购买的购物车项
        cartItemMapper.deleteByIds(cartItems.stream().map(CartItem::getId).toList());

        // 更新用户优惠券关联订单
        if (dto.getCouponId() != null) {
            userCouponMapper.update(null,
                    new LambdaUpdateWrapper<UserCoupon>()
                            .eq(UserCoupon::getUserId, userId)
                            .eq(UserCoupon::getCouponId, dto.getCouponId())
                            .eq(UserCoupon::getStatus, 1)
                            .set(UserCoupon::getUsedOrderId, order.getId())
            );
        }

        return buildVO(order);
    }

    @Override
    public Page<OrderVO> pageByUser(Long userId, int page, int size, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreatedAt);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        Page<OrderVO> voPage = new Page<>(page, size, orderPage.getTotal());
        voPage.setRecords(orderPage.getRecords().stream().map(this::buildVO).toList());
        return voPage;
    }

    @Override
    public OrderVO getDetail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return buildVO(order);
    }

    @Override
    @Transactional
    public void cancel(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("只能取消待付款的订单");
        }
        order.setStatus(4);
        orderMapper.updateById(order);

        // 恢复库存
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
        for (OrderItem item : items) {
            Flower flower = flowerMapper.selectById(item.getFlowerId());
            if (flower != null) {
                flower.setStock(flower.getStock() + item.getQuantity());
                flower.setSales(flower.getSales() - item.getQuantity());
                flowerMapper.updateById(flower);
            }
        }
    }

    @Override
    public void confirm(Long userId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 2) {
            throw new BusinessException("只能确认收货已发货的订单");
        }
        order.setStatus(3);
        order.setCompletedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public Page<OrderVO> pageAll(int page, int size, Integer status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .orderByDesc(Order::getCreatedAt);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, size), wrapper);
        Page<OrderVO> voPage = new Page<>(page, size, orderPage.getTotal());
        voPage.setRecords(orderPage.getRecords().stream().map(this::buildVO).toList());
        return voPage;
    }

    @Override
    public void updateStatus(Long orderId, Integer status) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(status);
        if (status == 2) order.setShippedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    public void ship(Long orderId) {
        updateStatus(orderId, 2);
    }

    private String generateOrderNo() {
        return "FLW" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", (int) (Math.random() * 10000));
    }

    private OrderVO buildVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusText(STATUS_MAP.getOrDefault(order.getStatus(), "未知"));

        // 订单明细
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        vo.setItems(items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(item, itemVO);
            return itemVO;
        }).toList());

        // 优惠券名称
        if (order.getCouponId() != null) {
            Coupon coupon = couponMapper.selectById(order.getCouponId());
            if (coupon != null) {
                vo.setCouponName(coupon.getName());
            }
        }
        return vo;
    }
}

package com.flowershop.module.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.module.order.dto.OrderCreateDTO;
import com.flowershop.module.order.dto.OrderVO;

public interface OrderService {
    OrderVO create(Long userId, OrderCreateDTO dto);
    Page<OrderVO> pageByUser(Long userId, int page, int size, Integer status);
    OrderVO getDetail(Long orderId);
    void cancel(Long userId, Long orderId);
    void confirm(Long userId, Long orderId);
    Page<OrderVO> pageAll(int page, int size, Integer status);
    void updateStatus(Long orderId, Integer status);
    void ship(Long orderId);
}

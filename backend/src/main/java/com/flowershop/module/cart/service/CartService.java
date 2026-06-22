package com.flowershop.module.cart.service;

import com.flowershop.module.cart.dto.CartItemDTO;
import com.flowershop.module.cart.dto.CartItemVO;

import java.util.List;

public interface CartService {
    List<CartItemVO> list(Long userId);
    void add(Long userId, CartItemDTO dto);
    void updateQuantity(Long userId, Long cartItemId, Integer quantity);
    void updateSelected(Long userId, Long cartItemId, Integer selected);
    void selectAll(Long userId, Integer selected);
    void remove(Long userId, Long cartItemId);
    void clearSelected(Long userId);
}

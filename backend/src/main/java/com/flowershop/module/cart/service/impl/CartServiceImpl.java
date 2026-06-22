package com.flowershop.module.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.flowershop.common.BusinessException;
import com.flowershop.module.cart.dto.CartItemDTO;
import com.flowershop.module.cart.dto.CartItemVO;
import com.flowershop.module.cart.entity.CartItem;
import com.flowershop.module.cart.mapper.CartItemMapper;
import com.flowershop.module.cart.service.CartService;
import com.flowershop.module.flower.entity.Flower;
import com.flowershop.module.flower.mapper.FlowerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemMapper cartItemMapper;
    private final FlowerMapper flowerMapper;

    @Override
    public List<CartItemVO> list(Long userId) {
        List<CartItem> items = cartItemMapper.selectList(
                new LambdaQueryWrapper<CartItem>().eq(CartItem::getUserId, userId)
        );
        return items.stream().map(item -> {
            CartItemVO vo = new CartItemVO();
            vo.setId(item.getId());
            vo.setFlowerId(item.getFlowerId());
            vo.setQuantity(item.getQuantity());
            vo.setSelected(item.getSelected());

            Flower flower = flowerMapper.selectById(item.getFlowerId());
            if (flower != null) {
                vo.setFlowerName(flower.getName());
                vo.setFlowerImage(flower.getCoverImage());
                vo.setPrice(flower.getPrice());
                vo.setStock(flower.getStock());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void add(Long userId, CartItemDTO dto) {
        // 检查是否已在购物车中
        CartItem existItem = cartItemMapper.selectOne(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId)
                        .eq(CartItem::getFlowerId, dto.getFlowerId())
        );
        if (existItem != null) {
            // 已有则累加数量
            existItem.setQuantity(existItem.getQuantity() + dto.getQuantity());
            cartItemMapper.updateById(existItem);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setFlowerId(dto.getFlowerId());
            item.setQuantity(dto.getQuantity());
            item.setSelected(1);
            cartItemMapper.insert(item);
        }
    }

    @Override
    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem item = getOwnItem(userId, cartItemId);
        item.setQuantity(quantity);
        cartItemMapper.updateById(item);
    }

    @Override
    public void updateSelected(Long userId, Long cartItemId, Integer selected) {
        CartItem item = getOwnItem(userId, cartItemId);
        item.setSelected(selected);
        cartItemMapper.updateById(item);
    }

    @Override
    public void selectAll(Long userId, Integer selected) {
        cartItemMapper.update(null,
                new LambdaUpdateWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId)
                        .set(CartItem::getSelected, selected)
        );
    }

    @Override
    public void remove(Long userId, Long cartItemId) {
        getOwnItem(userId, cartItemId);
        cartItemMapper.deleteById(cartItemId);
    }

    @Override
    public void clearSelected(Long userId) {
        cartItemMapper.delete(
                new LambdaQueryWrapper<CartItem>()
                        .eq(CartItem::getUserId, userId)
                        .eq(CartItem::getSelected, 1)
        );
    }

    private CartItem getOwnItem(Long userId, Long cartItemId) {
        CartItem item = cartItemMapper.selectById(cartItemId);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        return item;
    }
}

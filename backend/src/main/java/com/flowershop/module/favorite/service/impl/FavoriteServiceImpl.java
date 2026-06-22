package com.flowershop.module.favorite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flowershop.common.BusinessException;
import com.flowershop.module.favorite.dto.FavoriteVO;
import com.flowershop.module.favorite.entity.Favorite;
import com.flowershop.module.favorite.mapper.FavoriteMapper;
import com.flowershop.module.favorite.service.FavoriteService;
import com.flowershop.module.flower.entity.Flower;
import com.flowershop.module.flower.mapper.FlowerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final FlowerMapper flowerMapper;

    @Override
    public void add(Long userId, Long flowerId) {
        // 检查是否已收藏
        long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getFlowerId, flowerId)
        );
        if (count > 0) {
            throw new BusinessException("已收藏该花卉");
        }
        // 检查花卉是否存在
        if (flowerMapper.selectById(flowerId) == null) {
            throw new BusinessException("花卉不存在");
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setFlowerId(flowerId);
        favoriteMapper.insert(favorite);
    }

    @Override
    public void remove(Long userId, Long flowerId) {
        favoriteMapper.delete(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getFlowerId, flowerId)
        );
    }

    @Override
    public List<FavoriteVO> list(Long userId) {
        List<Favorite> favorites = favoriteMapper.selectList(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .orderByDesc(Favorite::getCreatedAt)
        );
        return favorites.stream().map(fav -> {
            FavoriteVO vo = new FavoriteVO();
            vo.setId(fav.getId());
            vo.setFlowerId(fav.getFlowerId());
            vo.setCreatedAt(fav.getCreatedAt());

            Flower flower = flowerMapper.selectById(fav.getFlowerId());
            if (flower != null) {
                vo.setFlowerName(flower.getName());
                vo.setFlowerImage(flower.getCoverImage());
                vo.setPrice(flower.getPrice());
                vo.setOriginalPrice(flower.getOriginalPrice());
                vo.setSales(flower.getSales());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isFavorited(Long userId, Long flowerId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getFlowerId, flowerId)
        ) > 0;
    }
}

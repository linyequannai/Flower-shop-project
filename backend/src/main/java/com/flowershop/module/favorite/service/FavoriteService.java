package com.flowershop.module.favorite.service;

import com.flowershop.module.favorite.dto.FavoriteVO;

import java.util.List;

public interface FavoriteService {
    void add(Long userId, Long flowerId);
    void remove(Long userId, Long flowerId);
    List<FavoriteVO> list(Long userId);
    boolean isFavorited(Long userId, Long flowerId);
}

package com.flowershop.module.review.service;

import com.flowershop.common.PageResult;
import com.flowershop.module.review.dto.ReviewSaveDTO;
import com.flowershop.module.review.dto.ReviewVO;

import java.util.List;

public interface ReviewService {
    void create(Long userId, ReviewSaveDTO dto);
    PageResult<ReviewVO> pageByFlower(Long flowerId, int page, int size);
    List<ReviewVO> listByUser(Long userId);
    void delete(Long userId, Long reviewId);
    void adminDelete(Long reviewId);
}

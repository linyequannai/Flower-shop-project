package com.flowershop.module.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flowershop.common.BusinessException;
import com.flowershop.common.PageResult;
import com.flowershop.module.review.dto.ReviewSaveDTO;
import com.flowershop.module.review.dto.ReviewVO;
import com.flowershop.module.review.entity.Review;
import com.flowershop.module.review.mapper.ReviewMapper;
import com.flowershop.module.review.service.ReviewService;
import com.flowershop.module.user.entity.User;
import com.flowershop.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    @Override
    public void create(Long userId, ReviewSaveDTO dto) {
        Review review = new Review();
        BeanUtils.copyProperties(dto, review);
        review.setUserId(userId);
        reviewMapper.insert(review);
    }

    @Override
    public PageResult<ReviewVO> pageByFlower(Long flowerId, int page, int size) {
        Page<Review> reviewPage = reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getFlowerId, flowerId)
                        .orderByDesc(Review::getCreatedAt));

        List<ReviewVO> voList = reviewPage.getRecords().stream().map(r -> {
            ReviewVO vo = new ReviewVO();
            BeanUtils.copyProperties(r, vo);
            User user = userMapper.selectById(r.getUserId());
            if (user != null) {
                vo.setUsername(user.getNickname() != null ? user.getNickname() : user.getUsername());
                vo.setUserAvatar(user.getAvatar());
            }
            return vo;
        }).toList();

        return PageResult.of(reviewPage.getTotal(), page, size, voList);
    }

    @Override
    public List<ReviewVO> listByUser(Long userId) {
        List<Review> reviews = reviewMapper.selectList(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getUserId, userId)
                        .orderByDesc(Review::getCreatedAt));
        return reviews.stream().map(r -> {
            ReviewVO vo = new ReviewVO();
            BeanUtils.copyProperties(r, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long reviewId) {
        Review review = reviewMapper.selectById(reviewId);
        if (review == null || !review.getUserId().equals(userId)) {
            throw new BusinessException("评价不存在");
        }
        reviewMapper.deleteById(reviewId);
    }

    @Override
    public void adminDelete(Long reviewId) {
        reviewMapper.deleteById(reviewId);
    }
}

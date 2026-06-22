package com.flowershop.module.review.controller;

import com.flowershop.common.PageResult;
import com.flowershop.common.Result;
import com.flowershop.module.review.dto.ReviewSaveDTO;
import com.flowershop.module.review.dto.ReviewVO;
import com.flowershop.module.review.service.ReviewService;
import com.flowershop.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews")
    public Result<PageResult<ReviewVO>> listByFlower(@RequestParam Long flowerId,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        return Result.ok(reviewService.pageByFlower(flowerId, page, size));
    }

    @PostMapping("/reviews")
    public Result<Void> create(@CurrentUser Long userId, @Valid @RequestBody ReviewSaveDTO dto) {
        reviewService.create(userId, dto);
        return Result.ok();
    }

    @GetMapping("/reviews/mine")
    public Result<List<ReviewVO>> myReviews(@CurrentUser Long userId) {
        return Result.ok(reviewService.listByUser(userId));
    }

    @DeleteMapping("/reviews/{id}")
    public Result<Void> delete(@CurrentUser Long userId, @PathVariable Long id) {
        reviewService.delete(userId, id);
        return Result.ok();
    }

    @DeleteMapping("/admin/reviews/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> adminDelete(@PathVariable Long id) {
        reviewService.adminDelete(id);
        return Result.ok();
    }
}

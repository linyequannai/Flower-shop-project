package com.flowershop.module.favorite.controller;

import com.flowershop.common.Result;
import com.flowershop.module.favorite.dto.FavoriteVO;
import com.flowershop.module.favorite.service.FavoriteService;
import com.flowershop.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public Result<List<FavoriteVO>> list(@CurrentUser Long userId) {
        return Result.ok(favoriteService.list(userId));
    }

    @PostMapping
    public Result<Void> add(@CurrentUser Long userId, @RequestBody Map<String, Long> body) {
        favoriteService.add(userId, body.get("flowerId"));
        return Result.ok();
    }

    @DeleteMapping("/{flowerId}")
    public Result<Void> remove(@CurrentUser Long userId, @PathVariable Long flowerId) {
        favoriteService.remove(userId, flowerId);
        return Result.ok();
    }

    @GetMapping("/check/{flowerId}")
    public Result<Map<String, Boolean>> check(@CurrentUser Long userId, @PathVariable Long flowerId) {
        return Result.ok(Map.of("favorited", favoriteService.isFavorited(userId, flowerId)));
    }
}

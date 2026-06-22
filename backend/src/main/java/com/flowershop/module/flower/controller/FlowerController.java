package com.flowershop.module.flower.controller;

import com.flowershop.common.PageResult;
import com.flowershop.common.Result;
import com.flowershop.module.flower.dto.FlowerQueryDTO;
import com.flowershop.module.flower.dto.FlowerSaveDTO;
import com.flowershop.module.flower.dto.FlowerVO;
import com.flowershop.module.flower.service.FlowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlowerController {

    private final FlowerService flowerService;

    @GetMapping("/flowers")
    public Result<PageResult<FlowerVO>> list(FlowerQueryDTO query) {
        return Result.ok(flowerService.pageQuery(query));
    }

    @GetMapping("/flowers/hot")
    public Result<List<FlowerVO>> hot(@RequestParam(defaultValue = "8") int limit) {
        return Result.ok(flowerService.getHot(limit));
    }

    @GetMapping("/flowers/{id}")
    public Result<FlowerVO> detail(@PathVariable Long id) {
        return Result.ok(flowerService.getDetail(id));
    }

    @PostMapping("/admin/flowers")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<FlowerVO> create(@Valid @RequestBody FlowerSaveDTO dto) {
        return Result.ok(flowerService.create(dto));
    }

    @PutMapping("/admin/flowers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<FlowerVO> update(@PathVariable Long id, @Valid @RequestBody FlowerSaveDTO dto) {
        return Result.ok(flowerService.update(id, dto));
    }

    @PutMapping("/admin/flowers/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        flowerService.updateStatus(id, status);
        return Result.ok();
    }

    @DeleteMapping("/admin/flowers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        flowerService.delete(id);
        return Result.ok();
    }
}

package com.flowershop.module.category.controller;

import com.flowershop.common.Result;
import com.flowershop.module.category.dto.CategoryDTO;
import com.flowershop.module.category.dto.CategoryVO;
import com.flowershop.module.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public Result<List<CategoryVO>> listAll() {
        return Result.ok(categoryService.listAll());
    }

    @PostMapping("/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryVO> create(@Valid @RequestBody CategoryDTO dto) {
        return Result.ok(categoryService.create(dto));
    }

    @PutMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<CategoryVO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        return Result.ok(categoryService.update(id, dto));
    }

    @DeleteMapping("/admin/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.ok();
    }
}

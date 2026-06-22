package com.flowershop.module.category.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flowershop.module.category.dto.CategoryDTO;
import com.flowershop.module.category.dto.CategoryVO;
import com.flowershop.module.category.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    List<CategoryVO> listAll();
    CategoryVO create(CategoryDTO dto);
    CategoryVO update(Long id, CategoryDTO dto);
    void delete(Long id);
}

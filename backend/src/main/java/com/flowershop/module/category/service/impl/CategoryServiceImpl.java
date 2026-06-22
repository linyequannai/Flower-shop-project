package com.flowershop.module.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flowershop.common.BusinessException;
import com.flowershop.module.category.dto.CategoryDTO;
import com.flowershop.module.category.dto.CategoryVO;
import com.flowershop.module.category.entity.Category;
import com.flowershop.module.category.mapper.CategoryMapper;
import com.flowershop.module.category.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryVO> listAll() {
        List<Category> list = list(new LambdaQueryWrapper<Category>().orderByAsc(Category::getSortOrder));
        return list.stream().map(cat -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(cat, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public CategoryVO create(CategoryDTO dto) {
        long count = count(new LambdaQueryWrapper<Category>().eq(Category::getName, dto.getName()));
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        save(category);
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

    @Override
    public CategoryVO update(Long id, CategoryDTO dto) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        BeanUtils.copyProperties(dto, category, "id", "createdAt");
        updateById(category);
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

    @Override
    public void delete(Long id) {
        if (!removeById(id)) {
            throw new BusinessException("分类不存在");
        }
    }
}

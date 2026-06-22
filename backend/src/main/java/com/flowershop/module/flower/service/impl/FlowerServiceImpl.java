package com.flowershop.module.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flowershop.common.BusinessException;
import com.flowershop.common.PageResult;
import com.flowershop.module.category.entity.Category;
import com.flowershop.module.category.mapper.CategoryMapper;
import com.flowershop.module.flower.dto.FlowerQueryDTO;
import com.flowershop.module.flower.dto.FlowerSaveDTO;
import com.flowershop.module.flower.dto.FlowerVO;
import com.flowershop.module.flower.entity.Flower;
import com.flowershop.module.flower.mapper.FlowerMapper;
import com.flowershop.module.flower.service.FlowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlowerServiceImpl extends ServiceImpl<FlowerMapper, Flower> implements FlowerService {

    private final CategoryMapper categoryMapper;

    @Override
    public PageResult<FlowerVO> pageQuery(FlowerQueryDTO query) {
        LambdaQueryWrapper<Flower> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Flower::getStatus, 1);

        // 关键词搜索
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.like(Flower::getName, query.getKeyword());
        }
        // 分类筛选
        if (query.getCategoryId() != null) {
            wrapper.eq(Flower::getCategoryId, query.getCategoryId());
        }
        // 排序
        if ("sales".equals(query.getSort())) {
            wrapper.orderByDesc(Flower::getSales);
        } else if ("price_asc".equals(query.getSort())) {
            wrapper.orderByAsc(Flower::getPrice);
        } else if ("price_desc".equals(query.getSort())) {
            wrapper.orderByDesc(Flower::getPrice);
        } else {
            wrapper.orderByDesc(Flower::getCreatedAt);
        }

        Page<Flower> page = page(new Page<>(query.getPage(), query.getSize()), wrapper);

        // 批量获取分类名称
        List<Long> categoryIds = page.getRecords().stream()
                .map(Flower::getCategoryId).distinct().toList();
        Map<Long, String> categoryNameMap = categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<FlowerVO> voList = page.getRecords().stream().map(f -> {
            FlowerVO vo = new FlowerVO();
            BeanUtils.copyProperties(f, vo);
            vo.setCategoryName(categoryNameMap.getOrDefault(f.getCategoryId(), ""));
            return vo;
        }).toList();

        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    @Override
    public FlowerVO getDetail(Long id) {
        Flower flower = getById(id);
        if (flower == null) {
            throw new BusinessException("花卉不存在");
        }
        Category category = categoryMapper.selectById(flower.getCategoryId());
        FlowerVO vo = new FlowerVO();
        BeanUtils.copyProperties(flower, vo);
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }

    @Override
    public List<FlowerVO> getHot(int limit) {
        List<Flower> list = baseMapper.selectHot(limit);
        List<Long> categoryIds = list.stream().map(Flower::getCategoryId).distinct().toList();
        Map<Long, String> categoryNameMap = categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return list.stream().map(f -> {
            FlowerVO vo = new FlowerVO();
            BeanUtils.copyProperties(f, vo);
            vo.setCategoryName(categoryNameMap.getOrDefault(f.getCategoryId(), ""));
            return vo;
        }).toList();
    }

    @Override
    public FlowerVO create(FlowerSaveDTO dto) {
        Flower flower = new Flower();
        BeanUtils.copyProperties(dto, flower);
        if (flower.getStatus() == null) flower.setStatus(1);
        if (flower.getSales() == null) flower.setSales(0);
        save(flower);
        return getDetail(flower.getId());
    }

    @Override
    public FlowerVO update(Long id, FlowerSaveDTO dto) {
        Flower flower = getById(id);
        if (flower == null) {
            throw new BusinessException("花卉不存在");
        }
        BeanUtils.copyProperties(dto, flower, "id", "sales", "createdAt", "updatedAt");
        updateById(flower);
        return getDetail(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Flower flower = getById(id);
        if (flower == null) {
            throw new BusinessException("花卉不存在");
        }
        flower.setStatus(status);
        updateById(flower);
    }

    @Override
    public void delete(Long id) {
        if (!removeById(id)) {
            throw new BusinessException("花卉不存在");
        }
    }
}

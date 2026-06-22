package com.flowershop.module.flower.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flowershop.common.PageResult;
import com.flowershop.module.flower.dto.FlowerQueryDTO;
import com.flowershop.module.flower.dto.FlowerSaveDTO;
import com.flowershop.module.flower.dto.FlowerVO;
import com.flowershop.module.flower.entity.Flower;

import java.util.List;

public interface FlowerService extends IService<Flower> {
    PageResult<FlowerVO> pageQuery(FlowerQueryDTO query);
    FlowerVO getDetail(Long id);
    List<FlowerVO> getHot(int limit);
    FlowerVO create(FlowerSaveDTO dto);
    FlowerVO update(Long id, FlowerSaveDTO dto);
    void updateStatus(Long id, Integer status);
    void delete(Long id);
}

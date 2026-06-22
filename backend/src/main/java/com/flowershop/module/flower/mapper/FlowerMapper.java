package com.flowershop.module.flower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.module.flower.entity.Flower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FlowerMapper extends BaseMapper<Flower> {

    @Select("SELECT * FROM flower WHERE status = 1 ORDER BY sales DESC LIMIT #{limit}")
    List<Flower> selectHot(@Param("limit") int limit);
}

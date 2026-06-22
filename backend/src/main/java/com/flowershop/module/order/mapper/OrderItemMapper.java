package com.flowershop.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flowershop.module.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}

package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/22
 * @Description: 订单的持久层
 */

@Mapper
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
}
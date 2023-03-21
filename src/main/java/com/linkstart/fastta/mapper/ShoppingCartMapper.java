package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/21
 * @Description: 购物车实体类的持久层
 */

@Mapper
@Repository
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.Flavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品口味的持久层
 */

@Mapper
@Repository
public interface FlavorMapper extends BaseMapper<Flavor> {
}
package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: 套餐菜品关系持久层
 */

@Mapper
@Repository
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
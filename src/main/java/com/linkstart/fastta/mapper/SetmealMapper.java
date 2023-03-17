package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 套餐持久层接口
 */

@Mapper
@Repository
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
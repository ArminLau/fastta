package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.SetmealDish;
import com.linkstart.fastta.mapper.SetmealDishMapper;
import com.linkstart.fastta.mapper.SetmealMapper;
import com.linkstart.fastta.service.SetmealDishService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: 套餐菜品关系业务层接口实现类
 */

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Override
    public List<SetmealDish> getSetmealDishBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        return this.list(queryWrapper);
    }
}
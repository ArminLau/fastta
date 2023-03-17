package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Setmeal;
import com.linkstart.fastta.mapper.SetmealMapper;
import com.linkstart.fastta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 套餐业务层实现类
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Override
    public long getCountByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        return super.count(queryWrapper);
    }
}
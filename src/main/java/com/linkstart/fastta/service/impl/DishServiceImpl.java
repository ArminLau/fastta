package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.dto.DishDto;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.DishFlavor;
import com.linkstart.fastta.entity.Flavor;
import com.linkstart.fastta.mapper.DishMapper;
import com.linkstart.fastta.service.DishFlavorService;
import com.linkstart.fastta.service.DishService;
import com.linkstart.fastta.service.FlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品业务层接口实现类
 */

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private FlavorService flavorService;

    @Override
    public long getCountByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        return this.count(queryWrapper);
    }

    @Override
    @Transactional
    public boolean saveDish(DishDto dishDto) {
        boolean saveDish = this.save(dishDto);
        List<DishFlavor> dishFlavors = dishDto.getFlavors();

        //解析DishFlavor中的value值转换成单独的Flavor对象
        List<Flavor> flavors = new ArrayList<>();
        if(CollUtil.isNotEmpty(dishFlavors)){
            dishFlavors.stream().forEach(x -> {
                String flavorName = x.getName();
                String flavorValue = x.getValue();
                String[] options = flavorValue.substring(1, flavorValue.length() - 1).split(",");
                for (String option : options) {
                    flavors.add(new Flavor(flavorName, option.substring(1, option.length()-1)));
                }
            });
        }
        flavorService.addFlavor(flavors);

        //给DishFlavor对象配置菜品ID
        Long dishId = dishDto.getId();
        dishFlavors = dishFlavors.stream().map(x -> {x.setDishId(dishId); return x;}).collect(Collectors.toList());
        boolean saveDishFlavor = dishFlavorService.saveBatch(dishFlavors);
        return (saveDish && saveDishFlavor);
    }

    @Override
    public Page<Dish> queryDishPage(int pageNum, int pageSize, String name) {
        Page<Dish> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //如果菜品名条件有效则查询名称类似的菜品，否则查询全部
        queryWrapper.like(StrUtil.isNotEmpty(name), Dish::getName, name);
        //根据菜品的sort值升序，更新时间倒序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        return this.page(page, queryWrapper);
    }
}
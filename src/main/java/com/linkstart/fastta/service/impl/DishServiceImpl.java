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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public DishDto getDishWithFlavor(Long id) {
        Dish dish = this.getById(id);
        if(dish == null){
            return null;
        }
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorService.getDishFlavorByDishId(id));
        return dishDto;
    }

    @Override
    @Transactional
    public boolean updateDishWithFlavor(DishDto dishDto) {
        boolean updateDish = this.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> dbFlavors = dishFlavorService.getDishFlavorByDishId(dishDto.getId());
        if(CollUtil.isEmpty(flavors) && CollUtil.isEmpty(dbFlavors)){
            return updateDish;
        }

        boolean addFlavors = false;
        boolean delFlavors = false;
        Set<String> dbFlavorSet = new HashSet<>();
        Set<String> flavorSet = new HashSet<>();
        String separator = ":";

        if(CollUtil.isNotEmpty(dbFlavors)){
            dbFlavors.stream().forEach(x -> dbFlavorSet.add(x.getName() + separator + x.getValue()));
        }
        if(CollUtil.isNotEmpty(flavors)){
            flavors.stream().forEach(x -> flavorSet.add(x.getName() + separator + x.getValue()));
        }

        //查询需要新添加的口味
        Set<String> newFlavorSet = Stream.concat(dbFlavorSet.stream(), flavorSet.stream()).filter(x -> flavorSet.contains(x) && !dbFlavorSet.contains(x))
                .collect(Collectors.toSet());
        addFlavors = dishFlavorService.batchAddBySet(dishDto.getId(), newFlavorSet, separator);

        //查询需要删除的口味
        Set<String> delFlavorSet = Stream.concat(dbFlavorSet.stream(), flavorSet.stream()).filter(x -> !flavorSet.contains(x) && dbFlavorSet.contains(x))
                .collect(Collectors.toSet());
        delFlavors = dishFlavorService.batchDeleteBySet(dishDto.getId(), delFlavorSet, separator);

        return updateDish && addFlavors && delFlavors;
    }

    @Override
    @Transactional
    public boolean batchDeleteDish(List<Long> ids) {
        if(CollUtil.isEmpty(ids)) return true;

        //删除菜品风味信息
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId, ids);
        long count = dishFlavorService.count(flavorQueryWrapper);
        //如果菜品没有关联的风味信息，则不再删除
        boolean deleteDishFlavor = count == 0 ? true : dishFlavorService.remove(flavorQueryWrapper);

        //删除菜品信息
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        boolean deleteDish = this.remove(queryWrapper);

        return deleteDish && deleteDishFlavor;
    }

    @Override
    public boolean batchUpdateDishStatus(List<Long> ids, Integer status) {
        if(CollUtil.isEmpty(ids)) return true;
        List<Dish> dishes = new ArrayList<>();
        ids.stream().forEach(x -> {
            Dish dish = new Dish();
            dish.setId(x);
            dish.setStatus(status);
            dishes.add(dish);
        });
        return this.updateBatchById(dishes);
    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId, String name) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //查询指定菜品分类下的所有启售的菜品
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId).eq(Dish::getStatus, 1);
        //模糊查询相似菜名的菜品
        queryWrapper.like(StrUtil.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        return this.list(queryWrapper);
    }
}
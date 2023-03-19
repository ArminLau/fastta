package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.dto.DishDto;
import com.linkstart.fastta.dto.SetmealDto;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Setmeal;
import com.linkstart.fastta.entity.SetmealDish;
import com.linkstart.fastta.mapper.SetmealMapper;
import com.linkstart.fastta.service.CategoryService;
import com.linkstart.fastta.service.SetmealDishService;
import com.linkstart.fastta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 套餐业务层实现类
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public long getCountByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        return super.count(queryWrapper);
    }

    @Override
    public Page<Setmeal> getSetmealPage(int pageNum, int pageSize, String name) {
        Page<Setmeal> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //如果name为有效条件，根据name进行模糊查询
        queryWrapper.like(StrUtil.isNotEmpty(name), Setmeal::getName, name);
        //根据套餐分类和更新时间排序
        queryWrapper.orderByDesc(Setmeal::getCategoryId).orderByDesc(Setmeal::getUpdateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean addSetmealWithDish(SetmealDto setmealDto) {
        boolean addSetmeal = this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //如果套餐不包含菜品，直接返回
        if(CollUtil.isEmpty(setmealDishes)) return addSetmeal;

        //给所有的套餐菜品设置套餐ID
        setmealDishes.stream().map(x -> {
            x.setSetmealId(setmealDto.getId());
            return x;
        }).collect(Collectors.toList());
        boolean addSetmealDishes = setmealDishService.saveBatch(setmealDishes);
        return addSetmeal && addSetmealDishes;
    }

    @Override
    public SetmealDto getSetmealWithDish(Long setmealId) {
        Setmeal setmeal = this.getById(setmealId);
        if(setmeal == null) return null;

        //套餐Dto对象拷贝属性值
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        //套餐Dto对象设置菜品信息
        setmealDto.setSetmealDishes(setmealDishService.getSetmealDishBySetmealId(setmealId));
        return setmealDto;
    }

    @Override
    @Transactional
    public boolean updateSetmealWithDish(SetmealDto setmealDto) {
        boolean updateSetmeal = this.updateById(setmealDto);
        if(!updateSetmeal) return false;

        //查询数据库中套餐的菜品信息
        List<SetmealDish> dbSetmealDishes = setmealDishService.getSetmealDishBySetmealId(setmealDto.getId());

        //删除套餐中原有的菜品信息
        boolean delOldSetmealDishes = false;
        if(CollUtil.isNotEmpty(dbSetmealDishes)){
            List<Long> dbSetmealDishIds = dbSetmealDishes.stream().map(SetmealDish::getId).collect(Collectors.toList());
            delOldSetmealDishes = setmealDishService.removeBatchByIds(dbSetmealDishIds);
        }else {
            delOldSetmealDishes = true;
        }

        //添加修改后套餐中包含的菜品信息
        boolean addNewSetmealDishes = false;
        List<SetmealDish> newSetmealDishes = setmealDto.getSetmealDishes();
        if(CollUtil.isNotEmpty(newSetmealDishes)){
            newSetmealDishes.stream().forEach(x -> x.setSetmealId(setmealDto.getId()));
            addNewSetmealDishes = setmealDishService.saveBatch(newSetmealDishes);
        }else {
            addNewSetmealDishes = true;
        }
        //返回结果
        return updateSetmeal && delOldSetmealDishes && addNewSetmealDishes;
    }

    @Override
    public boolean batchUpdateSetmealStatus(List<Long> ids, Integer status) {
        if(CollUtil.isEmpty(ids)) return false;

        //将提供的多个套餐ID封装成套餐对象，并修改每个套餐对象的状态
        List<Setmeal> setmeals = ids.stream().map(x -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(x);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());

        return this.updateBatchById(setmeals);
    }

    @Override
    public boolean batchDeleteSetmeal(List<Long> ids) {
        if(CollUtil.isEmpty(ids)) return false;

        //从套餐菜品中间表中找到所有包含目标套餐ID的记录进行删除
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, ids);
        boolean delSetmealDish = setmealDishService.remove(queryWrapper);

        //从套餐表中删除指定的套餐
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId, ids);
        boolean delSetmeal = this.remove(setmealQueryWrapper);
        return delSetmeal && delSetmealDish;
    }
}
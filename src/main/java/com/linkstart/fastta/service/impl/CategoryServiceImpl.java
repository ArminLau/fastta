package com.linkstart.fastta.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.exception.SystemTransactionException;
import com.linkstart.fastta.mapper.CategoryMapper;
import com.linkstart.fastta.service.CategoryService;
import com.linkstart.fastta.service.DishService;
import com.linkstart.fastta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类业务层接口实现类
 */

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public boolean addCategory(Category category) {
        return save(category);
    }

    @Override
    public Page<Category> queryCategoryPage(int pageNum, int pageSize) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        return page(page, queryWrapper);
    }

    @Override
    public boolean deleteCategory(long categoryId) {
        long dishCount = dishService.getCountByCategoryId(categoryId);
        if(dishCount > 0){
            throw new SystemTransactionException("该菜品分类关联了"+dishCount+"个菜品，不允许删除!");
        }
        long setmealCount = setmealService.getCountByCategoryId(categoryId);
        if(setmealCount > 0){
            throw new SystemTransactionException("该套餐分类关联了"+setmealCount+"个套餐，不允许删除！");
        }
        return super.removeById(categoryId);
    }

    @Override
    public List<Category> queryCategory(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //查询是否有类型条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //查询是否有名称条件
        queryWrapper.like(!StrUtil.isEmpty(category.getName()), Category::getName, category.getName());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        return super.list(queryWrapper);
    }
}
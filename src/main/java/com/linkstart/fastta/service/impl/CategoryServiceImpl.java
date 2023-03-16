package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.mapper.CategoryMapper;
import com.linkstart.fastta.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类业务层接口实现类
 */

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
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
}
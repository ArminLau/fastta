package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.dto.DishDto;
import com.linkstart.fastta.entity.Dish;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品业务层接口
 */

public interface DishService extends IService<Dish> {
    /**
     * 根据菜品分类ID查询菜品的个数
     * @param categoryId
     * @return
     */
    long getCountByCategoryId(Long categoryId);

    /**
     * 保存菜品以及处理口味
     * @param dishDto
     * @return
     */
    boolean saveDish(DishDto dishDto);

    /**
     * 根据名称条件分页查询菜品
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    Page<Dish> queryDishPage(int pageNum, int pageSize, String name);

    /**
     * 根据提供的ID查询菜品信息
     * @param id
     * @return
     */
    DishDto getDishWithFlavor(Long id);

    /**
     * 更新菜品的信息
     * @param dishDto
     * @return
     */
    boolean updateDishWithFlavor(DishDto dishDto);

    /**
     * 根据提供的多个菜品ID批量删除菜品信息
     * @param ids
     * @return
     */
    boolean batchDeleteDish(List<Long> ids);

    /**
     * 根据提供的多个菜品ID批量更新菜品的售卖状态
     * @param ids
     * @return
     */
    boolean batchUpdateDishStatus(List<Long> ids, Integer status);
}
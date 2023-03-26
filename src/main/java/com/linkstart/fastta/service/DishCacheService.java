package com.linkstart.fastta.service;

import com.linkstart.fastta.entity.Dish;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/26
 * @Description: 菜品缓存操作接口
 */

public interface DishCacheService {
    /**
     * 获取Redis缓存中分类的所有启售菜品信息
     */
    List<? extends Dish> getDishByCategoryId(Dish dish, boolean withFlavor);

    /**
     * 将指定分类的菜品信息存储到Redis中
     */
    String setDishByCategoryId(List<? extends Dish> dishes, boolean dishStatus, boolean withFlavor);

    /**
     * 清除Redis缓存中指定分类的菜品信息
     * @param categoryIds
     * @return
     */
    boolean delDishByCategoryIds(Long... categoryIds);

    /**
     * 清除Redis缓存中所有分类的菜品信息
     * @return
     */
    boolean delDish();

    /**
     * 获取默认的分类菜品信息在Redis缓存中的过期时间
     * @return
     */
    long getDefaultExpireSeconds();
}
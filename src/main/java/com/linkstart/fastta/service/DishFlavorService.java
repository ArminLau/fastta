package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.DishFlavor;

import java.util.List;
import java.util.Set;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品口味业务层接口
 */

public interface DishFlavorService extends IService<DishFlavor> {
    /**
     * 根据菜品Id查询对应的口味
     * @param dishId
     * @return
     */
    List<DishFlavor> getDishFlavorByDishId(Long dishId);

    /**
     * 一次性批量删除DishFlavor记录
     * @param dishId 菜品ID
     * @param nameValueSet getName()+separator+getValue()的元素构成的集合
     * @param separator 分隔符
     * @return
     */
    boolean batchDeleteBySet(Long dishId, Set<String> nameValueSet, String separator);

    /**
     * 一次性批量添加DishFlavor记录
     * @param dishId
     * @param nameValueSet
     * @param separator
     * @return
     */
    boolean batchAddBySet(Long dishId, Set<String> nameValueSet, String separator);
}
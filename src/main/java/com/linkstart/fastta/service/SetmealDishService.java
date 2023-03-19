package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.SetmealDish;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: 套餐菜品管理业务层接口
 */

public interface SetmealDishService extends IService<SetmealDish> {
    /**
     * 根据套餐ID查询套餐内的菜品信息
     * @param setmealId
     * @return
     */
    List<SetmealDish> getSetmealDishBySetmealId(Long setmealId);
}
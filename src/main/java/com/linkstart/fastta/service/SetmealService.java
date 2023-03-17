package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.Setmeal;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 套餐业务层接口
 */

public interface SetmealService extends IService<Setmeal> {
    /**
     * 根据套餐分类ID查询套餐的个数
     * @param categoryId
     * @return
     */
    long getCountByCategoryId(Long categoryId);
}
package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.dto.SetmealDto;
import com.linkstart.fastta.entity.Setmeal;

import java.util.List;

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

    /**
     * 根据提供的套餐名称条件分页查询套餐信息
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    Page<Setmeal> getSetmealPage(int pageNum, int pageSize, String name);

    /**
     * 保存套餐及套餐内菜品的信息
     * @param setmealDto
     * @return
     */
    boolean addSetmealWithDish(SetmealDto setmealDto);

    /**
     * 根据套餐ID查询套餐及其包含所包含菜品的信息
     * @param setmealId
     * @return
     */
    SetmealDto getSetmealWithDish(Long setmealId);

    /**
     * 更新套餐的信息
     * @param setmealDto
     * @return
     */
    boolean updateSetmealWithDish(SetmealDto setmealDto);

    /**
     * 根据提供的套餐ID批量更新套餐的状态:
     * @param ids
     * @param status 1:启售 0:停售
     * @return
     */
    boolean batchUpdateSetmealStatus(List<Long> ids, Integer status);

    /**
     * 根据提供的套餐ID批量删除套餐及其关联的菜品信息
     * @param ids
     * @return
     */
    boolean batchDeleteSetmeal(List<Long> ids);
}
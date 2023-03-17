package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.Flavor;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品口味管理的业务层接口
 */

public interface FlavorService extends IService<Flavor> {
    /**
     * 批量添加菜品风味
     * @param flavors
     * @return
     */
    boolean addFlavor(List<Flavor> flavors);

    /**
     * 查询所有的菜品口味(相同口味的口味选择归并在一个对象中)
     * @return
     */
    List<Flavor> getFlavor();
}
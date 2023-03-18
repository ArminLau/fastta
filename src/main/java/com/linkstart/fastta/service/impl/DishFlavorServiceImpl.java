package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.DishFlavor;
import com.linkstart.fastta.mapper.DishFlavorMapper;
import com.linkstart.fastta.mapper.DishMapper;
import com.linkstart.fastta.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品口味业务层接口实现类
 */
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public List<DishFlavor> getDishFlavorByDishId(Long dishId) {
        //查询菜品的口味选择
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean batchDeleteBySet(Long dishId, Set<String> nameValueSet, String separator) {
        List<DishFlavor> flavors = nameValueSetToArray(dishId, nameValueSet, separator);
        if(CollUtil.isEmpty(flavors)) return true;
        for (DishFlavor flavor : flavors) {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId).eq(DishFlavor::getName, flavor.getName())
                    .eq(DishFlavor::getValue, flavor.getValue());
            if(!this.remove(queryWrapper)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean batchAddBySet(Long dishId, Set<String> nameValueSet, String separator) {
        List<DishFlavor> flavors = nameValueSetToArray(dishId, nameValueSet, separator);
        return CollUtil.isEmpty(flavors) ? true : this.saveBatch(flavors);
    }

    private List<DishFlavor> nameValueSetToArray(Long dishId, Set<String> nameValueSet, String separator){
        if(CollUtil.isEmpty(nameValueSet)){
            return null;
        }
        List<DishFlavor> dishFlavors = new ArrayList<>();
        for (String nameAndValue : nameValueSet) {
            String[] split = nameAndValue.split(separator);
            dishFlavors.add(new DishFlavor(dishId, split[0], split[1]));
        }
        return dishFlavors;
    }
}
package com.linkstart.fastta.dto;

import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Setmeal;
import com.linkstart.fastta.entity.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: Setmeal实体类的DTO类
 */

@Data
public class SetmealDto extends Setmeal {
    //套餐中包含的菜品
    private List<SetmealDish> setmealDishes = new ArrayList<>();

    //套餐所属的套餐分类名称
    private String categoryName;
}
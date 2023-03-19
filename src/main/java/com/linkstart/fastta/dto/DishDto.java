package com.linkstart.fastta.dto;

import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: Dish实体类的DTO封装类
 */
@Data
public class DishDto extends Dish {
    //菜品中包含的口味选择
    private List<DishFlavor> flavors = new ArrayList<>();

    //菜品所属的菜系名称
    private String categoryName;

    //菜品的选择个数
    private Integer copies;
}
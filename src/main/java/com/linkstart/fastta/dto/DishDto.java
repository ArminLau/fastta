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
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
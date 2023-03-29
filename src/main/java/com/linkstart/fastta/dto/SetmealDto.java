package com.linkstart.fastta.dto;

import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Setmeal;
import com.linkstart.fastta.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: Setmeal实体类的DTO类
 */

@Data
@ApiModel("套餐Dto")
public class SetmealDto extends Setmeal {
    @ApiModelProperty("套餐中包含的菜品")
    private List<SetmealDish> setmealDishes = new ArrayList<>();

    @ApiModelProperty("套餐所属的套餐分类名称")
    private String categoryName;
}
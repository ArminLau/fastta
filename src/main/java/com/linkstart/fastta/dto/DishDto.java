package com.linkstart.fastta.dto;

import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: Dish实体类的DTO封装类
 */
@Data
@ApiModel("菜品Dto")
public class DishDto extends Dish {
    @ApiModelProperty("菜品中包含的口味选择")
    private List<DishFlavor> flavors = new ArrayList<>();

    @ApiModelProperty("菜品所属的菜系名称")
    private String categoryName;

    @ApiModelProperty("菜品的选择个数")
    private Integer copies;
}
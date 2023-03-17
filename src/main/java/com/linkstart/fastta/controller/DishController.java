package com.linkstart.fastta.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.dto.DishDto;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Flavor;
import com.linkstart.fastta.service.CategoryService;
import com.linkstart.fastta.service.DishFlavorService;
import com.linkstart.fastta.service.DishService;
import com.linkstart.fastta.service.FlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品管理的控制层
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private FlavorService flavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R saveDish(@RequestBody DishDto dishDto){
        log.info("保存菜品:{}", dishDto.getName());
        return R.judge(dishService.saveDish(dishDto), "保存菜品成功", "保存菜品失败");
    }

    @GetMapping("/flavor")
    public R getFlavor(){
        return R.success(flavorService.getFlavor());
    }

    @GetMapping(value = "/page", params = {"page", "pageSize"})
    public R getDishPage(int page, int pageSize, String name){
        Page<Dish> rawPage = dishService.queryDishPage(page, pageSize, name);
        if(rawPage.getTotal() == 0){
            return R.success(rawPage);
        }
        Page<DishDto> processedPage = new Page<>();
        BeanUtils.copyProperties(rawPage, processedPage, "records");
        //获取categoryId和categoryName的Map
        List<Category> categories = categoryService.list();
        Map<Long,String> categoryMap = new HashMap<>();
        categories.stream().forEach(x -> categoryMap.put(x.getId(), x.getName()));

        //封装DishDto的数据并填充categoryName值
        List<DishDto> processedRecords = rawPage.getRecords().stream().map(x -> {
            DishDto processedRecord = new DishDto();
            BeanUtils.copyProperties(x, processedRecord);
            processedRecord.setCategoryName(categoryMap.get(processedRecord.getCategoryId()));
            return processedRecord;
        }).collect(Collectors.toList());

        processedPage.setRecords(processedRecords);

        return R.success(processedPage);
    }
}
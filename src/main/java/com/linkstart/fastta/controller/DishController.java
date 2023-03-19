package com.linkstart.fastta.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.dto.DishDto;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.entity.Employee;
import com.linkstart.fastta.entity.Flavor;
import com.linkstart.fastta.service.CategoryService;
import com.linkstart.fastta.service.DishFlavorService;
import com.linkstart.fastta.service.DishService;
import com.linkstart.fastta.service.FlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
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
        log.info("员工ID[{}]添加了新菜品: {}", ThreadContext.getOnlineUser().getId(), dishDto.getName());
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
        if(CollUtil.isNotEmpty(categories)){
            categories.stream().forEach(x -> categoryMap.put(x.getId(), x.getName()));
        }

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

    @PutMapping
    public R updateDish(@RequestBody DishDto dishDto){
        log.info("员工ID[{}]更新了菜品: {}", ThreadContext.getOnlineUser().getId(), dishDto.getId());
        return R.judge(dishService.updateDishWithFlavor(dishDto), "菜品信息更新成功", "菜品信息更新失败");
    }

    @GetMapping("/{id}")
    public R getDish(@PathVariable Long id){
        return R.success(dishService.getDishWithFlavor(id));
    }

    @PostMapping(value = "/status/{value}", params = {"ids"})
    public R updateDishStatus(@PathVariable Integer value, List<Long> ids){
        String operate = value == 1 ? "启售" : "停售";
        log.info("员工ID[{}]{}了以下菜品: {}", ThreadContext.getOnlineUser().getId(), operate, ids);
        return R.judge(dishService.batchUpdateDishStatus(ids, value), "已成功"+operate+"指定的菜品", operate+"指定菜品失败");
    }

    @DeleteMapping
    public R deleteDish(@RequestParam("ids") List<Long> ids){
        log.info("员工ID[{}]删除了以下菜品: {}", ThreadContext.getOnlineUser().getId(), ids);
        return R.judge(dishService.batchDeleteDish(ids), "已成功删除指定的菜品", "删除指定菜品失败");
    }

    @GetMapping(value = "/list")
    public R getDishByCategoryId(Long categoryId, String name){
        return R.success(dishService.getDishByCategoryId(categoryId, name));
    }
}
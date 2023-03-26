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
import com.linkstart.fastta.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("hasAnyAuthority('Admin','Employee')")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private FlavorService flavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishCacheService dishCacheService;

    @PostMapping
    public R saveDishWithFlavor(@RequestBody DishDto dishDto){
        log.info("员工ID[{}]添加了新菜品: {}", ThreadContext.getOnlineUser().getId(), dishDto.getName());
        //清除对应菜品分类的Redis缓存
        dishCacheService.delDishByCategoryIds(dishDto.getCategoryId());
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
        Dish dish = dishService.getById(dishDto.getId());
        if(dish == null){
            return R.error("菜品不存在，菜品信息更新失败");
        }
        //清除菜品更新前后所属分类的Redis缓存
        dishCacheService.delDishByCategoryIds(dishDto.getCategoryId(), dish.getCategoryId());
        return R.judge(dishService.updateDishWithFlavor(dishDto), "菜品信息更新成功", "菜品信息更新失败");
    }

    @GetMapping("/{id}")
    public R getDish(@PathVariable Long id){
        return R.success(dishService.getDishWithFlavor(id));
    }

    @PostMapping(value = "/status/{value}")
    public R updateDishStatus(@PathVariable Integer value, @RequestParam("ids") List<Long> ids){
        String operate = value == 1 ? "启售" : "停售";
        log.info("员工ID[{}]{}了以下菜品: {}", ThreadContext.getOnlineUser().getId(), operate, ids);
        //清除所有菜品分类的Redis缓存
        dishCacheService.delDish();
        return R.judge(dishService.batchUpdateDishStatus(ids, value), "已成功"+operate+"指定的菜品", operate+"指定菜品失败");
    }

    @DeleteMapping
    public R deleteDish(@RequestParam("ids") List<Long> ids){
        log.info("员工ID[{}]删除了以下菜品: {}", ThreadContext.getOnlineUser().getId(), ids);
        //清除所有菜品分类的Redis缓存
        dishCacheService.delDish();
        return R.judge(dishService.batchDeleteDish(ids), "已成功删除指定的菜品", "删除指定菜品失败");
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee', 'Customer')")
    @GetMapping("/list")
    public R getDishList(Dish dish, boolean withFlavor){
        //默认只查询启售的菜品
        dish.setStatus(1);
        List<? extends Dish> cacheDishes = dishCacheService.getDishByCategoryId(dish, withFlavor);
        if(CollUtil.isEmpty(cacheDishes)){
            //如果请求参数中的withFlavor为true, 则查询关联口味的菜品信息
            cacheDishes = withFlavor ? dishService.getDishByCategoryIdWithFlavor(dish) : dishService.getDishByCategoryId(dish);
            //将菜品信息存储入Redis
            dishCacheService.setDishByCategoryId(cacheDishes, true, withFlavor);
        }
        return R.success(cacheDishes);
    }
}
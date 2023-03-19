package com.linkstart.fastta.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.dto.SetmealDto;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.entity.Setmeal;
import com.linkstart.fastta.service.CategoryService;
import com.linkstart.fastta.service.SetmealDishService;
import com.linkstart.fastta.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: 套餐管理控制层
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/page", params = {"page", "pageSize"})
    public R getSetmealPage(int page, int pageSize, String name){
        Page<Setmeal> rawPage = setmealService.getSetmealPage(page, pageSize, name);
        //如果查询的记录值为0，直接返回page对象
        if(rawPage.getTotal() == 0) return R.success(rawPage);


        Page<SetmealDto> processedPage = new Page<>();
        //拷贝属性值到套餐Dto对象
        BeanUtils.copyProperties(rawPage, processedPage, "records");

        //查询所有的套餐分类，构造套餐ID套餐名称的Map
        Category category = new Category();
        category.setType(2);
        List<Category> categories = categoryService.queryCategory(category);
        Map<Long, String> categoryMap = new HashMap<>();
        if(CollUtil.isNotEmpty(categories)){
            categories.stream().forEach(x -> categoryMap.put(x.getId(), x.getName()));
        }

        //对分页的记录值进行处理
        List<Setmeal> records = rawPage.getRecords();
        List<SetmealDto> processedRecords = records.stream().map(x -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(x, setmealDto);
            setmealDto.setCategoryName(categoryMap.get(setmealDto.getCategoryId()));
            return setmealDto;
        }).collect(Collectors.toList());
        processedPage.setRecords(processedRecords);

        return R.success(processedPage);
    }

    @PostMapping
    public R saveSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("员工ID[{}]添加了新套餐: {}", ThreadContext.getOnlineUser().getId(), setmealDto.getName());
        return R.judge(setmealService.addSetmealWithDish(setmealDto), "成功添加套餐", "添加套餐失败");
    }

    @GetMapping("/{id}")
    public R getSetmeal(@PathVariable Long id){
        return R.success(setmealService.getSetmealWithDish(id));
    }

    @PutMapping
    public R updateSetmeal(@RequestBody SetmealDto setmealDto){
        log.info("员工ID[{}]添加了套餐: {}", ThreadContext.getOnlineUser().getId(), setmealDto.getId());
        return R.judge(setmealService.updateSetmealWithDish(setmealDto), "成功更新套餐", "更新套餐失败");
    }

    @PostMapping("/status/{value}")
    public R batchUpdateSetmealStatus(@PathVariable Integer value, @RequestParam List<Long> ids){
        String operate = value == 1 ? "启售" : "停售";
        log.info("员工ID[{}]{}了以下套餐: {}", ThreadContext.getOnlineUser().getId(), operate, ids);
        return R.judge(setmealService.batchUpdateSetmealStatus(ids, value), "成功"+operate+"指定的套餐", operate+"指定套餐失败");
    }

    @DeleteMapping
    public R batchDeleteSetmeal(@RequestParam List<Long> ids){
        log.info("员工ID[{}]删除了以下套餐: {}", ThreadContext.getOnlineUser().getId(), ids);
        return R.judge(setmealService.batchDeleteSetmeal(ids), "成功删除指定的套餐", "删除指定的套餐失败");
    }
}
package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类的控制层
 */

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R addCategory(@RequestBody Category category){
        String typeDesc = category.getCategoryTypeDesc();
        log.info("添加{}: {}", typeDesc, category.getName());
        boolean addSuccess = categoryService.addCategory(category);
        return R.judge(addSuccess, typeDesc+"添加成功", typeDesc+"添加失败");
    }

    @GetMapping(value = "/page", params = {"page", "pageSize"})
    public R queryCategoryPage(int page, int pageSize){
        return R.success(categoryService.queryCategoryPage(page, pageSize));
    }
}
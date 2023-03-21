package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Category;
import com.linkstart.fastta.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类的控制层
 */

@RestController
@RequestMapping("/category")
@Slf4j
@PreAuthorize("hasAnyAuthority('Admin','Employee')")
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

    @DeleteMapping
    public R deleteCategory(@RequestParam(value = "ids", required = true) Long categoryId){
        log.info("删除ID为{}的分类信息", categoryId);
        return R.judge(categoryService.deleteCategory(categoryId), "分类删除成功", "分类删除失败");
    }

    @PutMapping
    public R updateCategory(@RequestBody Category category){
        log.info("更新ID为{}的分类信息", category.getId());
        return R.judge(categoryService.updateById(category), "分类更新成功", "分类更新失败");
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('Admin','Employee', 'Customer')")
    public R queryCategory(Category category){
        return R.success(categoryService.queryCategory(category));
    }
}
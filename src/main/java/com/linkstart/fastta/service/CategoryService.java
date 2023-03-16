package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.Category;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类的业务层接口
 */

public interface CategoryService extends IService<Category> {
    /**
     * 添加分类
     * @param category
     * @return
     */
    boolean addCategory(Category category);

    /**
     * 分页查询分类项目
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Category> queryCategoryPage(int pageNum, int pageSize);
}
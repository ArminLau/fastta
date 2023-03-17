package com.linkstart.fastta.test.service;

import com.linkstart.fastta.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCategoryService {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testDeleteCategory(){
        System.out.println(categoryService.deleteCategory(1636272470033498113L));;
    }
}

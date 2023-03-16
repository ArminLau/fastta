package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 分类的持久层
 */

@Mapper
@Repository
public interface CategoryMapper extends BaseMapper<Category> {
}
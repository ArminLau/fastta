package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.DishFlavor;
import com.linkstart.fastta.mapper.DishFlavorMapper;
import com.linkstart.fastta.mapper.DishMapper;
import com.linkstart.fastta.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 菜品口味业务层接口实现类
 */
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
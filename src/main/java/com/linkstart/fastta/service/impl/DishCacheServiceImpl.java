package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.linkstart.fastta.entity.Dish;
import com.linkstart.fastta.service.DishCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Armin
 * @Date: 2023/3/26
 * @Description: 菜品缓存操作接口实现类
 */

@Service
public class DishCacheServiceImpl implements DishCacheService {
    @Autowired
    private RedisTemplate redisTemplate;

    //菜品信息默认的Redis缓存时间
    private static final Long DISH_EXPIRE_SECONDS = 60 * 60L;

    private static final String DISH_CACHE_PREFIX = "Dish";

    @Override
    public List<? extends Dish> getDishByCategoryId(Dish dish, boolean withFlavor) {
        String key = DISH_CACHE_PREFIX + "-" + dish.getCategoryId() + "-" + (dish.getStatus() == 1 ? "1" : "0") + "-" + (withFlavor ? "1" : "0");
        return (List<? extends Dish>) redisTemplate.opsForValue().get(key);
    }

    @Override
    public String setDishByCategoryId(List<? extends Dish> dishes, boolean dishStatus, boolean withFlavor) {
        if(CollUtil.isEmpty(dishes)){
            return null;
        }
        String key = DISH_CACHE_PREFIX + "-" + dishes.get(0).getCategoryId() + "-" +  (dishStatus ? "1" : "0") + "-" + (withFlavor ? "1" : "0");
        redisTemplate.opsForValue().set(key, dishes, DISH_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return key;
    }

    @Override
    public boolean delDishByCategoryIds(Long... categoryIds) {
        for (Long categoryId : categoryIds) {
            String pattern = DISH_CACHE_PREFIX + "-" + categoryId + "*";
            Set keys = redisTemplate.keys(pattern);
            redisTemplate.delete(keys);
            if(redisTemplate.hasKey(pattern)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean delDish() {
        String pattern = DISH_CACHE_PREFIX + "-*";
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
        return redisTemplate.hasKey(pattern);
    }

    @Override
    public long getDefaultExpireSeconds() {
        return DISH_EXPIRE_SECONDS;
    }
}
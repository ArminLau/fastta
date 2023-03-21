package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.entity.ShoppingCart;
import com.linkstart.fastta.mapper.ShoppingCartMapper;
import com.linkstart.fastta.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/21
 * @Description: 购物车业务层接口实现类
 */

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
        /**
         * 对购物车新条目的预处理
         */
        //如果添加过来的条目没有携带数量，则默认数量为1
        Integer number = shoppingCart.getNumber();
        if(number == null){
            shoppingCart.setNumber(1);
        }
        //给购物车条目配置客户ID
        shoppingCart.setUserId(ThreadContext.getOnlineUser().getId());

        //对购物车中的口味排序，便于后续的比较
        if(StrUtil.isNotEmpty(shoppingCart.getDishFlavor())){
            String[] flavors = shoppingCart.getDishFlavor().split(",");
            Arrays.sort(flavors);
            shoppingCart.setDishFlavor(String.join(",", flavors));
        }

        ShoppingCart dbShoppingCart = this.getOne(getShoppingCartQueryWrapper(shoppingCart));

        if(dbShoppingCart != null){
            //如果发现相同的购物车条目已经存在于数据库中，则将相应条目的数量加上新条目的数量
            dbShoppingCart.setNumber(dbShoppingCart.getNumber() + shoppingCart.getNumber());
            if(this.updateById(dbShoppingCart)){
                shoppingCart = dbShoppingCart;
            }
        }else {
            //确定购物车条目不存在于数据库，则做插入操作
            this.save(shoppingCart);
        }
        return shoppingCart;
    }

    @Override
    public List<ShoppingCart> getShoppingCarts() {
        Long clientId = ThreadContext.getOnlineUser().getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, clientId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean cleanShoppingCart() {
        Long clientId = ThreadContext.getOnlineUser().getId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, clientId);
        return this.remove(queryWrapper);
    }

    @Override
    public ShoppingCart removeShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setUserId(ThreadContext.getOnlineUser().getId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = getShoppingCartQueryWrapper(shoppingCart);
        ShoppingCart target = this.getOne(queryWrapper);
        //如果没有查询到对应的购物车条目，直接返回false
        if(target == null){
            return null;
        }

        //若购物车条目的数目大于1，更新其数目使其减一，否则直接删除
        boolean subSuccess = false;
        if(target.getNumber() > 1){
            target.setNumber(target.getNumber() - 1);
            subSuccess = this.updateById(target);
        }else {
            subSuccess = this.remove(queryWrapper);
        }
        return subSuccess ? target : null;
    }

    /**
     * 根据提供的购物车对象条件返回对应的QueryWrapper对象
     * @param shoppingCart
     * @return
     */
    private LambdaQueryWrapper<ShoppingCart> getShoppingCartQueryWrapper(ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据购物车条目所有者ID配置查询条件
        lambdaQueryWrapper.eq(shoppingCart.getUserId() != null, ShoppingCart::getUserId, shoppingCart.getUserId());
        //假如购物车条目是菜品，则根据菜品ID配置查询条件
        lambdaQueryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        //假如购物车条目是套餐，则根据套餐ID配置查询条件
        lambdaQueryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        //假如购物车条目的风味为有效值，则根据风味配置查询条件
        lambdaQueryWrapper.eq(StrUtil.isNotEmpty(shoppingCart.getDishFlavor()), ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        return lambdaQueryWrapper;
    }
}
package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.entity.ShoppingCart;

import java.util.List;

/**
 * @Author: Armin
 * @Date: 2023/3/21
 * @Description: 购物车的业务层接口
 */

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 将菜品或套餐添加到购物车
     * @param shoppingCart
     * @return
     */
    ShoppingCart addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 查询当前登录用户的购物车清单
     * @return
     */
    List<ShoppingCart> getShoppingCarts();

    /**
     * 清空购物车(重置，非下单)
     * @return
     */
    boolean cleanShoppingCart();

    /**
     * 根据提供的条件删除购物车的条目
     * @param shoppingCart
     * @return
     */
    ShoppingCart removeShoppingCart(ShoppingCart shoppingCart);
}
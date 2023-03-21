package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.ShoppingCart;
import com.linkstart.fastta.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @Author: Armin
 * @Date: 2023/3/21
 * @Description: 购物车的控制层
 */

@RestController
@RequestMapping("/shoppingCart")
@PreAuthorize("hasAuthority('Customer')")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R getShoppingCartList(){
        return R.success(shoppingCartService.getShoppingCarts());
    }

    @PostMapping("/add")
    public R addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.success(shoppingCartService.addShoppingCart(shoppingCart));
    }

    @DeleteMapping("/clean")
    public R cleanShoppingCart(){
        return R.judge(shoppingCartService.cleanShoppingCart(), "成功清空购物车", "清空购物车失败");
    }

    @PostMapping("/sub")
    public R cleanShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.success(shoppingCartService.removeShoppingCart(shoppingCart));
    }
}
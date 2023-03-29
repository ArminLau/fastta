package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.ShoppingCart;
import com.linkstart.fastta.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "顾客的购物车管理接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation("获取顾客自己的购物车清单")
    @GetMapping("/list")
    public R getShoppingCartList(){
        return R.success(shoppingCartService.getShoppingCarts());
    }

    @ApiOperation("将菜品或套餐加入购物车")
    @PostMapping("/add")
    public R addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.success(shoppingCartService.addShoppingCart(shoppingCart));
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public R cleanShoppingCart(){
        return R.judge(shoppingCartService.cleanShoppingCart(), "成功清空购物车", "清空购物车失败");
    }

    @ApiOperation("将菜品或套餐移出购物车")
    @PostMapping("/sub")
    public R cleanShoppingCart(@RequestBody ShoppingCart shoppingCart){
        return R.success(shoppingCartService.removeShoppingCart(shoppingCart));
    }
}
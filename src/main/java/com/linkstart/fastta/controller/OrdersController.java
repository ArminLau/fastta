package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.dto.OrderPageDto;
import com.linkstart.fastta.entity.Orders;
import com.linkstart.fastta.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: Armin
 * @Date: 2023/3/22
 * @Description: 订单管理的控制层
 */

@RestController
@RequestMapping("/order")
@PreAuthorize("hasAuthority('Customer')")
@Slf4j
@Api(tags = "订单管理接口")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @ApiOperation("提交订单")
    @PostMapping("/submit")
    public R submitOrder(@RequestBody Orders orders){
        return R.judge(ordersService.sumbitOrder(orders), "下单成功", "下单有异常，请重试");
    }

    @ApiOperation("分页查询所有的订单")
    @PostMapping("/page")
    @PreAuthorize("permitAll()")
    public R getOrderPage(@RequestBody OrderPageDto orderPageDto){
        return R.success(ordersService.getOrderPage(orderPageDto));
    }

    @ApiOperation("分页查询顾客自己的订单")
    @GetMapping(value = "/userPage", params = {"page", "pageSize"})
    public R getCurrentClientOrderPage(int page, int pageSize, boolean withDetail){
        return R.success(ordersService.getCurrentClientOrderPage(page, pageSize, withDetail));
    }

    @ApiOperation("顾客请求再来一单")
    @PostMapping("/again")
    public R requireOrderAgain(@RequestBody Orders orders){
        return R.success(ordersService.requireOrderAgain(orders.getId()));
    }
}
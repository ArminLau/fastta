package com.linkstart.fastta.controller;

import com.linkstart.fastta.common.R;
import com.linkstart.fastta.dto.OrderPageDto;
import com.linkstart.fastta.entity.Orders;
import com.linkstart.fastta.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R submitOrder(@RequestBody Orders orders){
        return R.judge(ordersService.sumbitOrder(orders), "下单成功", "下单有异常，请重试");
    }

    @PostMapping("/page")
    public R getOrderPage(@RequestBody OrderPageDto orderPageDto){
        return R.success(ordersService.getOrderPage(orderPageDto));
    }

    @GetMapping(value = "/userPage", params = {"page", "pageSize"})
    public R getCurrentClientOrderPage(int page, int pageSize, boolean withDetail){
        return R.success(ordersService.getCurrentClientOrderPage(page, pageSize, withDetail));
    }

    @PostMapping("/again")
    public R requireOrderAgain(@RequestBody Orders orders){
        return R.success(ordersService.requireOrderAgain(orders.getId()));
    }
}
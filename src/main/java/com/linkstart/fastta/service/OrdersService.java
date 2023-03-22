package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.dto.OrderPageDto;
import com.linkstart.fastta.entity.Orders;

import java.time.LocalDateTime;

/**
 * @Author: Armin
 * @Date: 2023/3/22
 * @Description: 订单的业务层接口
 */

public interface OrdersService extends IService<Orders> {
    /**
     * 客户提交订单逻辑处理
     * @param orders
     * @return
     */
    boolean sumbitOrder(Orders orders);

    /**
     * 分页查询所有订单
     */
    Page<Orders> getOrderPage(OrderPageDto orderPageDto);

    /**
     * 分页查询当前登录用户的订单信息
     * @param pageNum
     * @param pageSize
     * @param withDetail 是否需要携带详细订单信息
     * @return
     */
    Page<Orders> getCurrentClientOrderPage(int pageNum, int pageSize, boolean withDetail);

    /**
     * 处理用户再来一单的请求，将对应的订单中包含的菜品和套餐追加到客户的购物车
     * @param orderId
     * @return
     */
    boolean requireOrderAgain(Long orderId);
}
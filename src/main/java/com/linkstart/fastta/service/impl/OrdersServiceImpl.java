package com.linkstart.fastta.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.common.ThreadContext;
import com.linkstart.fastta.dto.OrderPageDto;
import com.linkstart.fastta.entity.*;
import com.linkstart.fastta.exception.SystemTransactionException;
import com.linkstart.fastta.mapper.OrdersMapper;
import com.linkstart.fastta.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author: Armin
 * @Date: 2023/3/22
 * @Description: 订单详情的业务层接口实现类
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public boolean sumbitOrder(Orders orders) {
        //订单操作者ID
        Long clientId = ThreadContext.getOnlineUser().getId();
        /**
         * 预处理订单对象
         */
        if(orders == null) return false;
        //设置订单状态为未付款
        orders.setStatus(1);
        //设置订单ID和订单号
        orders.setId(IdWorker.getId());
        orders.setNumber(String.valueOf(orders.getId()));
        //设置下单时间
        orders.setOrderTime(LocalDateTime.now());

        /**
         * 配置订单收货人相关信息
         */
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook == null){
            throw new SystemTransactionException("当前收获地址无效，请重新设定默认的收获地址");
        }
        //设置订单联系人号码
        orders.setPhone(addressBook.getPhone());
        //设置订单收货地址
        orders.setAddress((StrUtil.isEmpty(addressBook.getProvinceName()) ? "" : addressBook.getProvinceName())
                        + (StrUtil.isEmpty(addressBook.getCityName()) ? "" : addressBook.getCityName())
                        + (StrUtil.isEmpty(addressBook.getDistrictName()) ? "" : addressBook.getDistrictName())
                        + (StrUtil.isEmpty(addressBook.getDetail()) ? "" : addressBook.getDetail())
                );
        //设置收件人名称
        orders.setConsignee(addressBook.getConsignee());

        //获取订单所有者的购物车条目
        List<ShoppingCart> carts = shoppingCartService.getShoppingCarts();
        if(CollUtil.isEmpty(carts)){
            throw new SystemTransactionException("当前购物车为空，下单失败");
        }


        /**
         * 写入订单详细表及计算订单金额
         */
        AtomicInteger amount = new AtomicInteger(0);
        //根据购物车条目封装订单详情对象集合
        List<OrderDetail> orderDetails = carts.stream().map(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setName(cart.getName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            //遍历一个购物车对象就将其金额添加到订单总额中
            amount.addAndGet(cart.getAmount().multiply(new BigDecimal(cart.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //设置订单总金额
        orders.setAmount(new BigDecimal(amount.get()));
        //写入订单详情表
        boolean insertOrderDetails = orderDetailService.saveBatch(orderDetails);
        /**
         * 配置订单所有者信息
         */
        User client = userService.getById(clientId);
        orders.setUserName(client.getName());
        orders.setUserId(clientId);

        /**
         * 写入订单表
         */
        boolean insertOrder = this.save(orders);

        //清空当前登录用户的购物
        boolean cleanShoppingCart = shoppingCartService.cleanShoppingCart();
        //返回结果
        return insertOrderDetails && insertOrder && cleanShoppingCart;
    }

    @Override
    public Page<Orders> getOrderPage(OrderPageDto orderPageDto) {
        //构建分页对象
        Page page = new Page(orderPageDto.getPage(), orderPageDto.getPageSize());

        System.out.println(StrUtil.isNotEmpty(orderPageDto.getNumber()));
        System.out.println(orderPageDto.getNumber());
        //构建查询条件
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //订单号模糊匹配
        queryWrapper.like(StrUtil.isNotEmpty(orderPageDto.getNumber()), Orders::getNumber, orderPageDto.getNumber());
        //查询时间匹配
        queryWrapper.gt(orderPageDto.getBeginTime() != null, Orders::getOrderTime, orderPageDto.getBeginTime());
        queryWrapper.lt(orderPageDto.getEndTime() != null, Orders::getOrderTime, orderPageDto.getEndTime());

        return this.page(page, queryWrapper);
    }

    @Override
    public Page<Orders> getCurrentClientOrderPage(int pageNum, int pageSize, boolean withDetail) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        //构建查询条件，按下单时间倒序排序
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, ThreadContext.getOnlineUser().getId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        Page<Orders> orders = this.page(page, queryWrapper);
        //如果查询的结果不为空且要求返回订单详细信息则处理以下逻辑
        if(withDetail && CollUtil.isNotEmpty(orders.getRecords())){
            List<Orders> ordersList = orders.getRecords();
            //得到所有订单的ID
            List<Long> ordersIds = ordersList.stream().map(x -> {
                return x.getId();
            }).collect(Collectors.toList());
            //根据订单ID得到相关联的订单详情信息
            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(OrderDetail::getOrderId, ordersIds);
            List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
            if(CollUtil.isNotEmpty(orderDetails)){
                //封装订单的详细信息
                ordersList.stream().map(x -> {
                    //查询得到订单的详情信息
                    List<OrderDetail> details = orderDetails.stream().filter(y -> y.getOrderId().equals(x.getId())).collect(Collectors.toList());
                    x.setOrderDetails(details);
                    return x;
                }).collect(Collectors.toList());
                //将封装了订单详细信息的订单集合赋值给page对象
                orders.setRecords(ordersList);
            }
        }
        return orders;
    }

    @Override
    @Transactional
    public boolean requireOrderAgain(Long orderId) {
        //查询订单的订单详细信息
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
        if(CollUtil.isEmpty(orderDetails)){
            throw new SystemTransactionException("对应的订单中不包含任何菜品或者套餐，请求失败");
        }

        //清空用户的购物车
        shoppingCartService.cleanShoppingCart();

        //根据订单的详细信息构建购物车对象，添加到用户购物车中
        orderDetails.stream().forEach(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setName(x.getName());
            shoppingCart.setAmount(x.getAmount());
            shoppingCart.setImage(x.getImage());
            shoppingCart.setDishId(x.getDishId());
            shoppingCart.setSetmealId(x.getSetmealId());
            shoppingCart.setDishFlavor(x.getDishFlavor());
            shoppingCart.setNumber(x.getNumber());
            shoppingCartService.addShoppingCart(shoppingCart);
        });

        return true;
    }
}
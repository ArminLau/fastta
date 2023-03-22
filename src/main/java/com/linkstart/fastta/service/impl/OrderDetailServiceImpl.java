package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.entity.OrderDetail;
import com.linkstart.fastta.mapper.OrderDetailMapper;
import com.linkstart.fastta.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: Armin
 * @Date: 2023/3/22
 * @Description: 订单的业务层接口实现类
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
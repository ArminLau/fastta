package com.linkstart.fastta.dto;

import com.linkstart.fastta.common.JacksonObjectMapper;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: Armin
 * @Date: 2023/3/23
 * @Description: 订单查询的DTO封装类
 */
@Data
public class OrderPageDto {
    //当前页
    private Integer page;

    //每页显示条数
    private Integer pageSize;

    //订单号
    private String number;

    //订单查询起始时间
    private LocalDateTime beginTime;

    //订单查询终止时间
    private LocalDateTime endTime;
}
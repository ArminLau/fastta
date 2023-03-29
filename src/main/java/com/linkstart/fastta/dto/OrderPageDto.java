package com.linkstart.fastta.dto;

import com.linkstart.fastta.common.JacksonObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author: Armin
 * @Date: 2023/3/23
 * @Description: 订单查询的DTO封装类
 */
@Data
@ApiModel("订单Dto")
public class OrderPageDto {
    @ApiModelProperty("当前页")
    private Integer page;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    @ApiModelProperty("订单号")
    private String number;

    @ApiModelProperty("订单查询起始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty("订单查询终止时间")
    private LocalDateTime endTime;
}
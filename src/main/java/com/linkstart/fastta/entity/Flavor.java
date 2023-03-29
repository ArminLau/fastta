package com.linkstart.fastta.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("菜品口味")
public class Flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @JsonIgnore
    private Long id;

    @ApiModelProperty("口味名称")
    private String name;

    @ApiModelProperty("口味选择")
    @JsonIgnore
    @TableField(value = "`option`") //需要反引号，否则Mybatis-Plus会将option字段视为关键字，查询时会报错
    private String option;

    @ApiModelProperty("可选的口味集合")
    @TableField(exist = false)
    private List<String> value;

    @ApiModelProperty("创建时间")
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @ApiModelProperty("最近的修改人")
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    public Flavor(String name, String option){
        this.name = name;
        this.option = option;
    }

    public Flavor(String name, List<String> options){
        this.name = name;
        this.value = options;
    }
}

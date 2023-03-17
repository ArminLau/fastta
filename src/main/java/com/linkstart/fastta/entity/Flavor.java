package com.linkstart.fastta.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 菜品口味
 */
@Data
@NoArgsConstructor
public class Flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    //口味名称
    private String name;

    //口味选择
    @JsonIgnore
    @TableField(value = "`option`") //需要反引号，否则Mybatis-Plus会将option字段视为关键字，查询时会报错
    private String option;

    //口味选择的集合
    @TableField(exist = false)
    private List<String> value;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

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

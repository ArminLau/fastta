package com.linkstart.fastta.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.linkstart.fastta.common.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * @Author: Armin
 * @Date: 2023/3/15
 * @Description: MybatisPlus相关的配置类
 */
@Configuration
@Slf4j
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //配置分页拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 自定义元数据对象处理器
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                Long operatorId = getOperatorId();
                log.debug("数据插入公共字段填充，当前操作者ID: {}", operatorId);
                metaObject.setValue("createTime", LocalDateTime.now());
                metaObject.setValue("updateTime", LocalDateTime.now());
                metaObject.setValue("createUser", operatorId);
                metaObject.setValue("updateUser", operatorId);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                Long operatorId = getOperatorId();
                log.debug("数据更新公共字段填充，当前操作者ID: {}", operatorId);
                metaObject.setValue("updateTime", LocalDateTime.now());
                metaObject.setValue("updateUser", operatorId);
            }

            private Long getOperatorId(){
                MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return userDetails.getId();
            }
        };
    }
}
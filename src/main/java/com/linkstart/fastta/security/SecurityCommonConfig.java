package com.linkstart.fastta.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: SpringSecurity相关的通用配置类
 */
@Configuration
public class SecurityCommonConfig {
    //配置用户密码加密类
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //配置SpringSecurity过滤白名单
    @Bean
    public SecurityIgnoreUrlsConfig securityIgnoreUrlsConfig(){
        return new SecurityIgnoreUrlsConfig();
    }

    //配置自定义未登录EntryPoint
    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    //配置自定义权限不足处理类
    @Bean
    public RestAccessDeniedHandler restAccessDeniedHandler(){
        return new RestAccessDeniedHandler();
    }
}
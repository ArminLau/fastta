package com.linkstart.fastta.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: SpringSecurity总配置类
 */

@EnableWebSecurity
//使能注解@PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    @Autowired
    private SecurityIgnoreUrlsConfig securityIgnoreUrlsConfig;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                            //SpringSecurity白名单放行
                            for (String url : securityIgnoreUrlsConfig.getUrls()) {
                                authorize.requestMatchers(new AntPathRequestMatcher(url)).permitAll();
                            }
                            authorize.anyRequest().authenticated();
                        }
                )
                // 关闭跨站请求防护
                .csrf()
                .disable()
                //配置响应头X-Frame-Options为SAMEORIGIN
                .headers(headers -> headers.frameOptions().sameOrigin())
                //禁用默认登录页面
                .formLogin()
                .disable()
                //自定义权限异常处理
                .exceptionHandling(exceptionHander -> exceptionHander
                        //自定义权限不足处理类
                        .accessDeniedHandler(restAccessDeniedHandler)
                        //自定义未登录EntryPoint
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/**/logout")).invalidateHttpSession(true));
        return http.build();
    }
}
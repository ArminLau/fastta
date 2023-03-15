package com.linkstart.fastta.security;

import com.alibaba.fastjson.JSON;
import com.linkstart.fastta.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Armin
 * @Date: 2023/3/15
 * @Description: 自定义未登录服务器返回结果
 */

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("拦截到未登录用户请求需授权资源: {}", request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(JSON.toJSONString(R.unauthorized()));
        response.getWriter().flush();
    }
}
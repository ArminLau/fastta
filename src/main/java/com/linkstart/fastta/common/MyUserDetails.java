package com.linkstart.fastta.common;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 自定义的Spring Security的UserDetails封装接口
 */

public interface MyUserDetails extends UserDetails {
    //获取当前用户的ID
    Long getId();
}
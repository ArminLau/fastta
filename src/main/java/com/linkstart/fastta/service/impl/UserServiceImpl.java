package com.linkstart.fastta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.User;
import com.linkstart.fastta.mapper.UserMapper;
import com.linkstart.fastta.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 用户业务层接口实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = getUserByPhone(phone);
        if(user == null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            this.save(user);
        }
        return user;
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        return getOne(queryWrapper);
    }

    @Override
    public R login(String phone) {
        UserDetails userDetails = loadUserByUsername(phone);
        if(!userDetails.isEnabled()){
            return R.error("您已被平台列为黑名单，禁止登录!");
        }
        //顾客登录成功后将顾客登录信息写入securityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("手机号为{}的用户登录成功", phone);
        return R.success("登录成功");
    }
}
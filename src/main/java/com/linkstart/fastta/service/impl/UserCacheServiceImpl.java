package com.linkstart.fastta.service.impl;

import com.linkstart.fastta.service.UserCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Armin
 * @Date: 2023/3/26
 * @Description: 顾客缓存操作接口实现类
 */

@Service
public class UserCacheServiceImpl implements UserCacheService {
    //默认的手机号验证码在Redis缓存中的过期时间
    private static final Long USER_PHONE_CAPTCHA_EXPIRE_SECONDS = 60*5L;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String getPhoneCaptcha(String phone) {
        return (String) redisTemplate.opsForValue().get(phone);
    }

    @Override
    public boolean setPhoneCaptcha(String phone, String code) {
        //如果短信发送成功，将验证码储存到Redis中并配置缓存过期时间
        redisTemplate.opsForValue().set(phone, code, USER_PHONE_CAPTCHA_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return redisTemplate.hasKey(phone);
    }

    @Override
    public boolean delPhoneCaptcha(String phone) {
        redisTemplate.delete(phone);
        return !redisTemplate.hasKey(phone);
    }

    @Override
    public long getPhoneCaptchaExpireSeconds(String phone) {
        //返回值为-1，则说明该键不存在
        return redisTemplate.getExpire(phone);
    }

    @Override
    public long getDefaultExpireSeconds() {
        return USER_PHONE_CAPTCHA_EXPIRE_SECONDS;
    }
}
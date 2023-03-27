package com.linkstart.fastta.service;

/**
 * @Author: Armin
 * @Date: 2023/3/26
 * @Description: 顾客缓存操作接口
 */

public interface UserCacheService {
    /**
     * 查询指定手机号的验证码
     * @param phone
     * @return
     */
    String getPhoneCaptcha(String phone);

    /**
     * 将指定手机号码的验证码存储到Redis缓存
     * @param phone
     * @param code
     * @return
     */
    boolean setPhoneCaptcha(String phone, String code);

    /**
     * 删除指定手机号在Redis缓存中的验证码
     * @param phone
     * @return
     */
    boolean delPhoneCaptcha(String phone);

    /**
     * 获取当前手机号中的验证码在Redis缓存中的过期时间
     * @param phone
     * @return
     */
    long getPhoneCaptchaExpireSeconds(String phone);

    /**
     * 获取默认的手机验证码在Redis缓存中的过期时间
     * @return
     */
    long getDefaultExpireSeconds();
}
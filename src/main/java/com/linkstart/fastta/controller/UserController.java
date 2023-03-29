package com.linkstart.fastta.controller;

import cn.hutool.core.util.StrUtil;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.service.UserCacheService;
import com.linkstart.fastta.service.UserService;
import com.linkstart.fastta.service.impl.UserCacheServiceImpl;
import com.linkstart.fastta.util.SmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 用户的控制层
 */
@RestController
@RequestMapping("/user")
@PreAuthorize("hasAuthority('Customer')")
@Api(tags = "顾客管理接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserCacheService userCacheService;

    @ApiOperation("顾客登录请求发送手机登录验证码")
    @PostMapping("/sms")
    @PreAuthorize("permitAll()")
    public R sendLoginVerificationCode(@RequestParam String phone, HttpServletRequest request) throws Exception{
        //限制同一个手机号频繁请求发送验证码短信
        long expireSeconds = userCacheService.getPhoneCaptchaExpireSeconds(phone);
        if(expireSeconds > 0){
            return R.error(userCacheService.getDefaultExpireSeconds() + "秒内只允许请求发送一次验证码，请"+expireSeconds+"秒后重试");
        }
        String code = SmsUtil.sendMessage(phone, 6);
        if(StrUtil.isNotEmpty(code)){
            userCacheService.setPhoneCaptcha(phone, code);
        }
        return R.judge(code!=null, "验证码发送成功，请注意查收", "短信发送失败");
    }

    @ApiOperation("顾客登录账户")
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public R login(@RequestBody Map<String,String> map, HttpServletRequest request){
        String phone = map.get("phone");
        String code = map.get("code");
        if(StrUtil.isEmpty(phone) || StrUtil.isEmpty(code)) return R.error("请提供有效的电话号码和验证码");
        if(code.equals(userCacheService.getPhoneCaptcha(phone))){
            //手机验证码验证成功后从Redis删除验证码
            userCacheService.delPhoneCaptcha(phone);
            return userService.login(phone);
        }
        return R.error("登录异常");
    }

    @ApiOperation("顾客登出账户")
    @PostMapping("/logout")
    public R logout(){
        return R.success(null);
    }
}
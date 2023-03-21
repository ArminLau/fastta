package com.linkstart.fastta.controller;

import cn.hutool.core.util.StrUtil;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.service.UserService;
import com.linkstart.fastta.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 用户的控制层
 */
@RestController
@RequestMapping("/user")
@PreAuthorize("hasAuthority('Customer')")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sms")
    @PreAuthorize("permitAll()")
    public R sendLoginVerificationCode(@RequestParam String phone, HttpServletRequest request) throws Exception{
        String code = SmsUtil.sendMessage(phone, 6);
        if(StrUtil.isNotEmpty(code)){
            //如果短信发送成功，将验证码储存到session中
            request.getSession().setAttribute("code", code);
        }
        return R.judge(code!=null, "验证码发送成功，请注意查收", "短信发送失败");
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public R login(@RequestBody Map<String,String> map, HttpServletRequest request){
        String phone = map.get("phone");
        String code = map.get("code");
        if(StrUtil.isEmpty(phone) || StrUtil.isEmpty(code)) return R.error("请提供有效的电话号码和验证码");
        if(code.equals(request.getSession().getAttribute("code"))){
            //手机验证码验证成功后自动清除
            request.getSession().removeAttribute("code");
            return userService.login(phone);
        }
        return R.error("登录异常");
    }
}
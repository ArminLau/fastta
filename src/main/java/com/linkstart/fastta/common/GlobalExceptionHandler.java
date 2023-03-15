package com.linkstart.fastta.common;

import com.linkstart.fastta.exception.UsernameExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Armin
 * @Date: 2023/3/15
 * @Description: 自定义全局的异常处理类
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    //添加用户时检测到用户名存在抛出此异常
    @ExceptionHandler(UsernameExistException.class)
    public R usernameExistExceptionHandler(UsernameExistException exception){
        log.error("用户名:{}已存在，用户添加失败", exception.getUsername());
        return R.validateFailed(exception.getMessage());
    }
}
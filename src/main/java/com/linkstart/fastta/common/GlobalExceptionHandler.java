package com.linkstart.fastta.common;

import cn.hutool.core.util.ReUtil;
import com.linkstart.fastta.exception.SystemTransactionException;
import com.linkstart.fastta.exception.UsernameExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

/**
 * @Author: Armin
 * @Date: 2023/3/15
 * @Description: 自定义全局的异常处理类
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {
    //检测到用户名存在相同出现异常时进行处理
    @ExceptionHandler(UsernameExistException.class)
    public R usernameExistExceptionHandler(UsernameExistException exception){
        log.error("用户名:{}已存在，用户添加失败", exception.getUsername());
        return R.validateFailed(exception.getMessage());
    }

    //检测到系统业务异常时进行处理
    @ExceptionHandler(SystemTransactionException.class)
    public R systemTransactionExceptionHandler(SystemTransactionException exception){
        log.error(exception.getMessage());
        return R.validateFailed(exception.getMessage());
    }

    //处理Sql写入遇到完整性约束异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException exception){
        String errorMsg = exception.getMessage();
        log.error("An exception occurred: ", exception);

        //处理唯一约束异常
        String uniqueStr = ReUtil.get("Duplicate entry '[\\s\\S]*' for key", errorMsg, 0);
        if(uniqueStr != null){
            String duplicateKey = uniqueStr.substring(17, uniqueStr.length()-9);
            return R.validateFailed(duplicateKey + " 已存在, 请选择另一个合法值重试!");
        }

        return R.error("系统繁忙，请稍后重试！");
    }
}
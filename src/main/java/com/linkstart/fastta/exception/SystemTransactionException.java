package com.linkstart.fastta.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: Armin
 * @Date: 2023/3/17
 * @Description: 系统业务异常
 */

@Slf4j
public class SystemTransactionException extends RuntimeException {
    public SystemTransactionException(String message){
        super(message);
    }
}
package com.linkstart.fastta.exception;

/**
 * @Author: Armin
 * @Date: 2023/3/15
 * @Description: 自定义异常类:当检测到系统中存在同名的username时抛出此异常
 */
public class UsernameExistException extends RuntimeException{
    private String username;

    public UsernameExistException(String username) {
        super("账号:"+username+ "已存在，请重新选择!");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getUsername() {
        return username;
    }
}
package com.linkstart.fastta.common;

/**
 * 通用返回结果的状态码
 */
public enum StatusCode {
    SUCCESS(1, "操作成功"),
    FAILED(0, "服务器内部错误"),
    VALIDATE_FAILED(3, "请求校验失败"),
    UNAUTHORIZED(4, "未登录"),
    FORBIDDEN(5, "权限不足");

    private Integer code;
    private String message;

    private StatusCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

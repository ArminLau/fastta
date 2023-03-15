package com.linkstart.fastta.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> judge(boolean isSuccess, String successMsg, String failedMsg){
        if(isSuccess){
            return success(successMsg);
        }else {
            return error(failedMsg);
        }
    }

    public static <T> R<T> success(T object) {
        return handleStatusCode(object, null, StatusCode.SUCCESS);
    }

    public static <T> R<T> success(String msg){
        return handleStatusCode(null, msg, StatusCode.SUCCESS);
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public static <T> R<T> unauthorized(){
        return handleStatusCode(null, null, StatusCode.UNAUTHORIZED);
    }

    public static <T> R<T> forbidden(){
        return handleStatusCode(null, null, StatusCode.FORBIDDEN);
    }

    public static <T> R<T> validateFailed(String msg){
        return handleStatusCode(null, msg, StatusCode.VALIDATE_FAILED);
    }

    private static <T> R<T> handleStatusCode(T object, String msg, StatusCode statusCode){
        R<T> r = new R<T>();
        r.data = object;
        r.code = statusCode.getCode();
        r.msg = (msg != null && msg.length() > 0) ? msg : statusCode.getMessage();
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}

package com.linkstart.fastta.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Armin
 * @Date: 2023/3/16
 * @Description: 基于ThreadLocal的封装工具类，用于同一线程的变量共享
 */

public class ThreadContext {
    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();

    public static void setContext(String key, Object value){
        Map<String, Object> contextMap = threadLocal.get();
        if(contextMap == null){
            contextMap = new HashMap<>();
        }
        contextMap.put(key, value);
        threadLocal.set(contextMap);
    }

    public static Object getContext(String key){
        Map<String, Object> contextMap = threadLocal.get();
        return (contextMap == null || !contextMap.containsKey(key)) ? null : contextMap.get(key);
    }

    public static boolean deleteContext(String key){
        Map<String, Object> contextMap = threadLocal.get();
        if(contextMap == null || !contextMap.containsKey(key)){
            return false;
        }
        contextMap.remove(key);
        return true;
    }
}
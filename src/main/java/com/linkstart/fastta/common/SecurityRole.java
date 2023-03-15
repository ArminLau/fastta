package com.linkstart.fastta.common;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: 描述用户角色的枚举类
 */

public enum  SecurityRole {
    ADMIN("Admin"),
    EMPLOYEE("Employee"),
    CUSTOMER("Customer");

    private String roleName;

    private SecurityRole(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
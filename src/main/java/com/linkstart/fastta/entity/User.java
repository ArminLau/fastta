package com.linkstart.fastta.entity;

import cn.hutool.core.util.StrUtil;
import com.linkstart.fastta.common.MyUserDetails;
import com.linkstart.fastta.common.SecurityRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@ApiModel("顾客")
public class User implements MyUserDetails {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("绑定手机号")
    private String phone;

    @ApiModelProperty("性别: 0 女 1 男")
    private String sex;

    @ApiModelProperty("身份证号")
    private String idNumber;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("账户状态： 0 禁用，1 正常")
    private Integer status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //用户登录默认赋予顾客权限
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(SecurityRole.CUSTOMER.getRoleName()));
        return grantedAuthorities;
    }

    public String getName(){
        return StrUtil.isEmpty(this.name) ? this.phone : this.name;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1 ? true : false;
    }
}

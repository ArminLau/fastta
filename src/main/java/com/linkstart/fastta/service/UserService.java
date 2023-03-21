package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 顾客登录的业务层接口
 */

public interface UserService extends IService<User>, UserDetailsService {
    /**
     * 根据顾客的电话号码查询顾客信息
     * @param phone
     * @return
     */
    User getUserByPhone(String phone);

    /**
     * 处理顾客登录流程
     * @param phone
     * @return
     */
    R login(String phone);
}
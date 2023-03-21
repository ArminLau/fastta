package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/20
 * @Description: 用户实体类的持久层接口
 */

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
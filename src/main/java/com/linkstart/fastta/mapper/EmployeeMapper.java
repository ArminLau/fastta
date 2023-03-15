package com.linkstart.fastta.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkstart.fastta.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: 雇员实体类的Mapper
 */

@Mapper
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
}
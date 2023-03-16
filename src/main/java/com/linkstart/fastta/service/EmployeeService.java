package com.linkstart.fastta.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: 雇员实体类的Service接口
 */

public interface EmployeeService extends IService<Employee>, UserDetailsService {
    /**
     * 处理用户登录
     * @param employee
     * @return
     */
    R handleLogin(Employee employee);

    /**
     * 处理登录用户注销
     * @return
     */
    boolean handleLogout();

    /**
     * 添加用户
     * @param employee
     * @return
     */
    boolean addEmployee(Employee employee);

    /**
     * 分页查询员工
     * @param pageNum
     * @param pageSize
     * @param name 员工的姓名
     * @return
     */
    Page<Employee> queryEmployeePage(int pageNum, int pageSize, String name);

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    boolean updateEmployee(Employee employee);

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    Employee getEmployeeByUsername(String username);
}
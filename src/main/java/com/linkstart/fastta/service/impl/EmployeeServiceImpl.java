package com.linkstart.fastta.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Employee;
import com.linkstart.fastta.exception.UsernameExistException;
import com.linkstart.fastta.mapper.EmployeeMapper;
import com.linkstart.fastta.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: EmployeeService的实现类
 */

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    //添加Employee时使用的默认密码
    @Value("${fastta.manage.employee-default-password}")
    private String employeeDefaultPassword;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee getEmployeeByUsername(String username){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        return getOne(queryWrapper);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = getEmployeeByUsername(username);
        return employee;
    }

    @Override
    public R handleLogin(Employee employee) {
        UserDetails userDetails = loadUserByUsername(employee.getUsername());
        if(userDetails == null || !passwordEncoder.matches(employee.getPassword(), userDetails.getPassword())){
            //用户不存在或者登录密码错误
            return R.error("用户名或密码错误");
        }else if(!userDetails.isEnabled()){
            //用户账号被管理员锁定
            return R.error("用户被禁用，请联系管理员");
        }
        //用户登录成功后将用户登录信息写入securityContext
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);
        return R.success(userDetails);
    }

    @Override
    public boolean handleLogout() {
        return true;
    }

    @Override
    public boolean addEmployee(Employee employee) {
        if(getEmployeeByUsername(employee.getUsername()) != null){
            throw new UsernameExistException(employee.getUsername());
        }
        employee.setPassword(employeeDefaultPassword);
        return save(packEmployee(employee));
    }

    @Override
    public Page<Employee> queryEmployeePage(int pageNum, int pageSize, String name) {
        Page<Employee> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //如果name为无效值,则不参与关键词查询
        queryWrapper.like(!StrUtil.isBlank(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return updateById(packEmployee(employee));
    }

    /**
     * 员工实体类的封装，确保员工的信息添加或改动合法
     * @param employee
     * @return
     */
    private Employee packEmployee(Employee employee){
        Employee employeeWrapper = new Employee();
        employeeWrapper.setId(employee.getId());
        employeeWrapper.setName(employee.getName());
        employeeWrapper.setUsername(employee.getUsername());
        if(StrUtil.isEmpty(employee.getPassword())){
            employeeWrapper.setPassword(null);
        }else {
            employeeWrapper.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        employeeWrapper.setPhone(employee.getPhone());
        employeeWrapper.setSex(employee.getSex());
        employeeWrapper.setIdNumber(employee.getIdNumber());
        employeeWrapper.setStatus(employee.getStatus());
        return employeeWrapper;
    }
}
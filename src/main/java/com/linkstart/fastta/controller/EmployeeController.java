package com.linkstart.fastta.controller;


import com.linkstart.fastta.common.MyUserDetails;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Employee;
import com.linkstart.fastta.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Armin
 * @Date: 2023/3/14
 * @Description: 员工类的控制层
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R login(@RequestBody Employee employee){
        return employeeService.handleLogin(employee);
    }

    @PostMapping("/logout")
    public R logout(Authentication authentication){
        employeeService.handleLogout();
        return R.success(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Admin')")
    public R addEmployee(@RequestBody Employee employee){
        boolean addSuccess = employeeService.addEmployee(employee);
        String[] message = {"成功添加员工账户: "+employee.getUsername(), "新增员工账户失败"};
        log.info(addSuccess ? message[0] : message[1]);
        return R.judge(addSuccess, message[0], message[1]);
    }

    @GetMapping(value = "/page", params = {"page", "pageSize"})
    public R queryEmployeePage(int page, int pageSize, String name){
        return R.success(employeeService.queryEmployeePage(page, pageSize, name));
    }

    @PutMapping
    public R updateEmployee(@RequestBody Employee employee){
        boolean updateSuccess = employeeService.updateEmployee(employee);
        String[] message = {"成功更新员工账户: "+employee.getId(), "更新员工账户失败"};
        log.info(updateSuccess ? message[0] : message[1]+":"+employee.getId());
        return R.judge(updateSuccess, message[0], message[1]);
    }

    @GetMapping("/{id}")
    public R queryEmployee(@PathVariable long id){
        Employee employee = employeeService.getById(id);
        return employee != null ? R.success(employee) : R.error("没有查到到对应的员工信息");
    }

    @PostMapping("/{username}")
    public R ifUsernameExist(@PathVariable String username){
        Employee employee = employeeService.getEmployeeByUsername(username);
        return R.success(employee != null);
    }

    @PostMapping("/account")
    @PreAuthorize("#employee.username == authentication.principal.username")
    public R updateAccount(@RequestBody Employee employee, Authentication authentication){
        Long id = ((MyUserDetails) authentication.getPrincipal()).getId();
        employee.setId(id);
        if(employeeService.updateEmployee(employee)){
            return R.success(employeeService.getById(id));
        }else {
            return R.error("账号信息更新失败");
        }
    }
}
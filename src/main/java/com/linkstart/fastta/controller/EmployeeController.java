package com.linkstart.fastta.controller;


import com.linkstart.fastta.common.MyUserDetails;
import com.linkstart.fastta.common.R;
import com.linkstart.fastta.entity.Employee;
import com.linkstart.fastta.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@PreAuthorize("hasAuthority('Admin')")
@Api(tags = "员工管理接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation("员工登录账户")
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public R login(@RequestBody Employee employee){
        return employeeService.handleLogin(employee);
    }

    @ApiOperation("员工登出账户")
    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    public R logout(Authentication authentication){
        employeeService.handleLogout();
        return R.success(null);
    }

    @ApiOperation("添加员工账户")
    @PostMapping
    public R addEmployee(@RequestBody Employee employee){
        boolean addSuccess = employeeService.addEmployee(employee);
        String[] message = {"成功添加员工账户: "+employee.getUsername(), "新增员工账户失败"};
        log.info(addSuccess ? message[0] : message[1]);
        return R.judge(addSuccess, message[0], message[1]);
    }

    @ApiOperation("分页查询员工信息")
    @GetMapping(value = "/page", params = {"page", "pageSize"})
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    public R queryEmployeePage(int page, int pageSize, String name){
        return R.success(employeeService.queryEmployeePage(page, pageSize, name));
    }

    @ApiOperation("修改员工信息")
    @PutMapping
    public R updateEmployee(@RequestBody Employee employee){
        boolean updateSuccess = employeeService.updateEmployee(employee);
        String[] message = {"成功更新员工账户: "+employee.getId(), "更新员工账户失败"};
        log.info(updateSuccess ? message[0] : message[1]+":"+employee.getId());
        return R.judge(updateSuccess, message[0], message[1]);
    }

    @ApiOperation("根据ID查询相应员工的信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    public R queryEmployee(@PathVariable long id){
        Employee employee = employeeService.getById(id);
        return employee != null ? R.success(employee) : R.error("没有查到到对应的员工信息");
    }

    @ApiOperation("检查提供的员工账户名是否可用")
    @GetMapping(value = "/{username}", params = {"id"})
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    public R ifUsernameAvailable(@PathVariable String username, long id){
        log.debug("检测用户名[{}]是否可用", username);
        Employee employee = employeeService.getEmployeeByUsername(username);
        return R.success(employee == null || employee.getId() == id);
    }

    @ApiOperation("员工更新自己的个人信息")
    @PostMapping("/account")
    @PreAuthorize("#employee.username == authentication.principal.username and hasAnyAuthority('Admin','Employee')")
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
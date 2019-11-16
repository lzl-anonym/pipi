package com.anonym.module.admin.employee;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.employee.basic.EmployeeService;
import com.anonym.module.admin.employee.basic.domain.dto.*;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 员工管理
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_EMPLOYEE})
@OperateLog
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/admin/employee/query")
    @ApiOperation(value = "员工管理查询")
    public ResponseDTO<PageResultDTO<EmployeeVO>> query(@RequestBody EmployeeQueryDTO query) {
        return employeeService.selectEmployeeList(query);
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/admin/employee/add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddDTO emp) {
        LoginTokenDTO requestToken = SmartRequestTokenUtil.getRequestUser();
        return employeeService.addEmployee(emp, requestToken);
    }

    @ApiOperation(value = "禁用单个员工")
    @GetMapping("/admin/employee/updateStatus/{employeeId}/{status}")
    public ResponseDTO<String> updateStatus(@PathVariable("employeeId") Long employeeId, @PathVariable("status") Integer status) {
        return employeeService.updateStatus(employeeId, status);
    }

    @ApiOperation(value = "批量禁用")
    @PostMapping("/admin/employee/batchUpdateStatus")
    public ResponseDTO<String> batchUpdateStatus(@Valid @RequestBody EmployeeBatchUpdateStatusDTO batchUpdateStatusDTO) {
        return employeeService.batchUpdateStatus(batchUpdateStatusDTO);
    }

    @ApiOperation(value = "更新员工信息")
    @PostMapping("/admin/employee/update")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateDTO employeeUpdateDto) {
        return employeeService.updateEmployee(employeeUpdateDto);
    }

    @ApiOperation(value = "删除员工信息")
    @PostMapping("/admin/employee/delete/{employeeId}")
    public ResponseDTO<String> deleteEmployeeById(@PathVariable("employeeId") Long employeeId) {
        return employeeService.deleteEmployeeById(employeeId);
    }

    @ApiOperation(value = "单个员工角色授权")
    @PostMapping("/admin/employee/updateRoles")
    public ResponseDTO<String> updateRoles(@Valid @RequestBody EmployeeUpdateRolesDTO updateRolesDTO) {
        return employeeService.updateRoles(updateRolesDTO);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/admin/employee/updatePwd")
    public ResponseDTO<String> updatePwd(@Valid @RequestBody EmployeeUpdatePwdDTO updatePwdDTO) {
        LoginTokenDTO requestToken = SmartRequestTokenUtil.getRequestUser();
        return employeeService.updatePwd(updatePwdDTO, requestToken);
    }

    @ApiOperation(value = "通过部门id获取当前部门的人员&没有部门的人")
    @GetMapping("/admin/employee/listEmployeeByDeptId/{deptId}")
    public ResponseDTO<List<EmployeeVO>> listEmployeeByDeptId(@PathVariable Long deptId) {
        return employeeService.getEmployeeByDeptId(deptId);
    }

    @ApiOperation(value = "员工重置密码", notes = "@author lizongliang")
    @GetMapping("/admin/employee/resetPasswd/{employeeId}")
    public ResponseDTO resetPasswd(@PathVariable("employeeId") Integer employeeId) {
        return employeeService.resetPasswd(employeeId);
    }

}

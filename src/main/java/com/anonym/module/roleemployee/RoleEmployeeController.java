package com.anonym.module.roleemployee;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.role.domain.RoleBatchDTO;
import com.anonym.module.role.domain.RoleQueryDTO;
import com.anonym.module.role.domain.RoleSelectedDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色管理路由
 */
@Api(tags = {SwaggerTagConst.MANAGER_ROLE_USER})
@OperateLog
@RestController
public class RoleEmployeeController {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @ApiOperation(value = "获取角色成员-员工列表", notes = "获取角色成员-员工列表（分页）")
    @PostMapping("/role/listEmployee")

    public ResponseDTO<PageInfoDTO<EmployeeDTO>> listEmployeeByName(@Valid @RequestBody RoleQueryDTO queryDTO) {
        return roleEmployeeService.listEmployeeByName(queryDTO);
    }

    @ApiOperation(value = "根据角色id获取角色员工列表(无分页)", notes = "根据角色id获取角色成员-员工列表")
    @GetMapping("/role/listAllEmployee/{roleId}")

    public ResponseDTO<List<EmployeeDTO>> listAllEmployeeRoleId(@PathVariable Integer roleId) {
        return roleEmployeeService.getAllEmployeeByRoleId(roleId);
    }

    @ApiOperation(value = "从角色成员列表中移除员工", notes = "从角色成员列表中移除员工")
    @ApiImplicitParams({@ApiImplicitParam(name = "employeeId", value = "员工id", paramType = "query", required = true), @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "query",
            required = true)})
    @GetMapping("/role/removeEmployee")
    public ResponseDTO<String> removeEmployee(Integer employeeId, Integer roleId) {
        return roleEmployeeService.removeEmployeeRole(employeeId, roleId);
    }

    @ApiOperation(value = "从角色成员列表中批量移除员工", notes = "从角色成员列表中批量移除员工")
    @PostMapping("/role/removeEmployeeList")
    public ResponseDTO<String> removeEmployeeList(@Valid @RequestBody RoleBatchDTO removeDTO) {
        return roleEmployeeService.batchRemoveEmployeeRole(removeDTO);
    }

    @ApiOperation(value = "角色成员列表中批量添加员工", notes = "角色成员列表中批量添加员工")
    @PostMapping("/role/addEmployeeList")
    public ResponseDTO<String> addEmployeeList(@Valid @RequestBody RoleBatchDTO addDTO) {
        return roleEmployeeService.batchAddEmployeeRole(addDTO);
    }

    @ApiOperation(value = "通过员工id获取所有角色以及员工具有的角色", notes = "通过员工id获取所有角色以及员工具有的角色")
    @GetMapping("/role/getRoles/{employeeId}")
    @ApiImplicitParams({@ApiImplicitParam(name = "employeeId", value = "员工id", paramType = "path", required = true)})
    public ResponseDTO<List<RoleSelectedDTO>> getRoleByEmployeeId(@PathVariable Integer employeeId) {
        return roleEmployeeService.getRolesByEmployeeId(employeeId);
    }
}

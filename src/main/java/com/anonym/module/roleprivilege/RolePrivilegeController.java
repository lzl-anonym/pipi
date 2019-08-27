package com.anonym.module.roleprivilege;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.roleprivilege.domain.RolePrivilegeDTO;
import com.anonym.module.roleprivilege.domain.RolePrivilegeTreeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [ 与员工权限相关：角色权限关系、权限列表 ]
 */
@OperateLog
@RestController
@Api(tags = {SwaggerTagConst.MANAGER_ROLE_PRIVILEGE})
public class RolePrivilegeController {

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @ApiOperation(value = "更新角色权限", notes = "更新角色权限")
    @PostMapping("/privilege/updateRolePrivilege")
    public ResponseDTO<String> updateRolePrivilege(@Valid @RequestBody RolePrivilegeDTO updateDTO) {
        return rolePrivilegeService.updateRolePrivilege(updateDTO);
    }

    @ApiOperation(value = "获取角色可选的功能权限", notes = "获取角色可选的功能权限")
    @GetMapping("/privilege/listPrivilegeByRoleId/{roleId}")
    public ResponseDTO<RolePrivilegeTreeDTO> listPrivilegeByRoleId(@PathVariable("roleId") Integer roleId) {
        return rolePrivilegeService.listPrivilegeByRoleId(roleId);
    }

}

package com.anonym.module.admin.role.roleprivilege;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.role.roleprivilege.domain.dto.RolePrivilegeDTO;
import com.anonym.module.admin.role.roleprivilege.domain.dto.RolePrivilegeTreeVO;
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
@Api(tags = {SwaggerTagConst.Admin.MANAGER_ROLE_PRIVILEGE})
public class RolePrivilegeController {

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @ApiOperation(value = "更新角色权限", notes = "更新角色权限")
    @PostMapping("/admin/privilege/updateRolePrivilege")
    public ResponseDTO<String> updateRolePrivilege(@Valid @RequestBody RolePrivilegeDTO updateDTO) {
        return rolePrivilegeService.updateRolePrivilege(updateDTO);
    }

    @ApiOperation(value = "获取角色可选的功能权限", notes = "获取角色可选的功能权限")
    @GetMapping("/admin/privilege/listPrivilegeByRoleId/{roleId}")
    public ResponseDTO<RolePrivilegeTreeVO> listPrivilegeByRoleId(@PathVariable("roleId") Long roleId) {
        return rolePrivilegeService.listPrivilegeByRoleId(roleId);
    }

}

package com.anonym.module.role;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.role.domain.RoleAddDTO;
import com.anonym.module.role.domain.RoleDTO;
import com.anonym.module.role.domain.RoleUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理路由
 *
 */
@Api(tags = {SwaggerTagConst.MANAGER_ROLE})
@OperateLog
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "添加角色", notes = "添加角色")
    @PostMapping("/role/add")
    public ResponseDTO addRole(@Valid @RequestBody RoleAddDTO roleAddDTO) {
        return roleService.addRole(roleAddDTO);
    }

/*    @ApiOperation(value = "删除角色", notes = "根据id删除角色")
    @GetMapping("/role/delete/{roleId}")
    public ResponseDTO<String> deleteRole(@PathVariable("roleId") Long roleId) {
        return roleService.deleteRole(roleId);
    }*/

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PostMapping("/role/update")
    public ResponseDTO<String> updateRole(@Valid @RequestBody RoleUpdateDTO roleUpdateDTO) {
        return roleService.updateRole(roleUpdateDTO);
    }

    @ApiOperation(value = "获取角色数据", notes = "根据id获取角色数据")
    @GetMapping("/role/get/{roleId}")
    public ResponseDTO<RoleDTO> getRole(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @ApiOperation(value = "获取所有角色", notes = "获取所有角色数据")
    @GetMapping("/role/getAll")
    public ResponseDTO<List<RoleDTO>> getAllRole() {
        return roleService.getAllRole();
    }

}

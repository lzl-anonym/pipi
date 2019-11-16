package com.anonym.module.admin.privilege.controller;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.privilege.domain.dto.*;
import com.anonym.module.admin.privilege.service.PrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * [ 与员工权限相关：角色权限关系、权限列表 ]
 */
@OperateLog
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_PRIVILEGE})
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/admin/privilege/getAllUrl")
    @ApiOperation(value = "获取所有请求路径", notes = "获取所有请求路径")
    public ResponseDTO<List<PrivilegeRequestUrlVO>> getAllUrl() {
        return privilegeService.getPrivilegeUrlDTOList();
    }

    @ApiOperation(value = "菜单批量保存")
    @PostMapping("/admin/privilege/menu/batchSaveMenu")
    public ResponseDTO<String> menuBatchSave(@Valid @RequestBody List<PrivilegeMenuDTO> menuList) {
        return privilegeService.menuBatchSave(menuList);
    }

    @ApiOperation(value = "查询所有菜单项")
    @PostMapping("/admin/privilege/menu/queryAll")
    public ResponseDTO<List<PrivilegeMenuListVO>> queryAll() {
        return privilegeService.menuQueryAll();
    }

    @ApiOperation(value = "保存更新功能点")
    @PostMapping("/admin/privilege/function/saveOrUpdate")
    public ResponseDTO<String> functionSaveOrUpdate(@Valid @RequestBody PrivilegeFunctionDTO privilegeFunctionDTO) {
        return privilegeService.functionSaveOrUpdate(privilegeFunctionDTO);
    }

    @ApiOperation(value = "查询菜单功能点", notes = "更新")
    @PostMapping("/admin/privilege/function/query/{menuKey}")
    public ResponseDTO<List<PrivilegeFunctionVO>> functionQuery(@PathVariable String menuKey) {
        return privilegeService.functionQuery(menuKey);
    }

}

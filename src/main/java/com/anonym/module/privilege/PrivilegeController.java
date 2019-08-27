package com.anonym.module.privilege;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.privilege.domain.PrivilegeAddDTO;
import com.anonym.module.privilege.domain.PrivilegeDTO;
import com.anonym.module.privilege.domain.PrivilegeUpdateDTO;
import com.anonym.module.privilege.domain.PrivilegeUrlDTO;
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
@Api(tags = {SwaggerTagConst.COMMON_PRIVILEGE})
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping("/privilege/getAllUrl/{scope}")
    @ApiOperation(value = "获取所有路由", notes = "获取所有路由 scope 1管理端权限 2web端权限 @author sunboqiang")
    @ResponseBody
    public ResponseDTO<List<PrivilegeUrlDTO>> getAllUrl(@PathVariable("scope") Integer scope) {
        return ResponseDTO.succData(privilegeService.getPrivilegeUrlDTOList(scope));
    }

    @ApiOperation(value = "添加", notes = "添加")
    @PostMapping("/privilege/add")
    public ResponseDTO<String> add(@Valid @RequestBody PrivilegeAddDTO privilegeAddDTO) {
        return privilegeService.insert(privilegeAddDTO);
    }

    @ApiOperation(value = "更新", notes = "更新")
    @PostMapping("/privilege/update")
    public ResponseDTO<String> update(@Valid @RequestBody PrivilegeUpdateDTO privilegeUpdateDTO) {
        return privilegeService.update(privilegeUpdateDTO);
    }

    @ApiOperation(value = "删除", notes = "删除菜单权限")
    @PostMapping("/privilege/delete/{privilegeId}")
    public ResponseDTO<String> delPrivilege(@PathVariable("privilegeId") Integer privilegeId) {
        return privilegeService.delPrivilege(privilegeId);
    }

    /**
     * 查询菜单列表
     *
     * @return
     */
    @ApiOperation(value = "查询菜单列表", notes = "查询菜单列表  scope 1管理端权限 2web端权限")
    @GetMapping("/privilege/listPrivilege/{scope}")
    public ResponseDTO<List<PrivilegeDTO>> listPrivilege(@PathVariable("scope") Integer scope) {
        return privilegeService.listPrivilege(scope);
    }

    /**
     * 查询菜单对应的页面以及功能点
     *
     * @return
     */
    @ApiOperation(value = "查询菜单对应的页面以及功能点", notes = "查询菜单对应的页面以及功能点")
    @GetMapping("/privilege/listPrivilegeById/{id}")
    public ResponseDTO<PrivilegeDTO> listPrivilegeById(@PathVariable Integer id) {
        return privilegeService.listPrivilegeById(id);
    }

}

package com.anonym.module.admin.datascope;

import com.anonym.common.anno.AdminNoValidPrivilege;
import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.datascope.domain.dto.DataScopeAndViewTypeVO;
import com.anonym.module.admin.datascope.domain.dto.DataScopeBatchSetRoleDTO;
import com.anonym.module.admin.datascope.domain.dto.DataScopeSelectVO;
import com.anonym.module.admin.datascope.service.DataScopeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {SwaggerTagConst.Admin.MANAGER_DATA_SCOPE})
@OperateLog
@RestController
public class DataScopeController {

    @Autowired
    private DataScopeService dataScopeService;

    @ApiOperation(value = "获取当前系统所配置的所有数据范围")
    @GetMapping("/admin/admin/dataScope/list")
    @AdminNoValidPrivilege
    public ResponseDTO<List<DataScopeAndViewTypeVO>> dataScopeList() {
        return dataScopeService.dataScopeList();
    }

    @ApiOperation(value = "获取某角色所设置的数据范围")
    @GetMapping("/admin/dataScope/listByRole/{roleId}")
    @AdminNoValidPrivilege
    public ResponseDTO<List<DataScopeSelectVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return dataScopeService.dataScopeListByRole(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围")
    @PostMapping("/admin/dataScope/batchSet")
    @AdminNoValidPrivilege
    public ResponseDTO<String> dataScopeBatchSet(@RequestBody @Valid DataScopeBatchSetRoleDTO batchSetRoleDTO) {
        return dataScopeService.dataScopeBatchSet(batchSetRoleDTO);
    }

}

package com.anonym.module.log.operatelog;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogDTO;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_OPERATE_LOG})
@OperateLog
public class EmployeeOperateLogController {

    @Autowired
    private EmployeeOperateLogService userOperateLogService;

    @ApiOperation(value = "分页查询")
    @PostMapping("/admin/userOperateLog/page/query")
    public ResponseDTO<PageResultDTO<EmployeeOperateLogDTO>> queryByPage(@RequestBody EmployeeOperateLogQueryDTO queryDTO) {
        return userOperateLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "删除")
    @GetMapping("/admin/userOperateLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return userOperateLogService.delete(id);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/admin/userOperateLog/detail/{id}")
    public ResponseDTO<EmployeeOperateLogDTO> detail(@PathVariable("id") Long id) {
        return userOperateLogService.detail(id);
    }
}

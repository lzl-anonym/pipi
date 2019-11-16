package com.anonym.module.log.loginlog;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeQueryDTO;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogDTO;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * [ 用户登录日志 ]
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_USER_LOGIN_LOG})
@OperateLog
public class EmployeeLoginLogController {

    @Autowired
    private EmployeeLoginLogService userLoginLogService;

    @ApiOperation(value = "分页查询用户登录日志")
    @PostMapping("/admin/userLoginLog/page/query")
    public ResponseDTO<PageResultDTO<EmployeeLoginLogDTO>> queryByPage(@RequestBody EmployeeLoginLogQueryDTO queryDTO) {
        return userLoginLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "删除用户登录日志")
    @GetMapping("/admin/userLoginLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return userLoginLogService.delete(id);
    }

    @ApiOperation(value = "查询员工在线状态")
    @PostMapping("/admin/userOnLine/query")
    public ResponseDTO<PageResultDTO<EmployeeVO>> queryUserOnLine(@RequestBody @Valid EmployeeQueryDTO queryDTO) {
        return userLoginLogService.queryUserOnLine(queryDTO);
    }

}

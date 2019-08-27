package com.anonym.module.userloginlog;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [ 用户登录日志 ]

 */
@RestController
@Api(tags = {SwaggerTagConst.MANAGER_USER_LOGIN_LOG})
@OperateLog
public class UserLoginLogController {

    @Autowired
    private UserLoginLogService userLoginLogService;

    @ApiOperation(value = "分页查询用户登录日志")
    @PostMapping("/userLoginLog/page/query")
    public ResponseDTO<PageInfoDTO<UserLoginLogDTO>> queryByPage(@RequestBody UserLoginLogQueryDTO queryDTO) {
        return userLoginLogService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "删除用户登录日志")
    @GetMapping("/userLoginLog/delete/{id}")
    public ResponseDTO<String> delete(@PathVariable("id") Long id) {
        return userLoginLogService.delete(id);
    }

}

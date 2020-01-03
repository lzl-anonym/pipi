package com.anonym.module.admin.employee.login;

import com.anonym.common.anno.AdminAuthorityLevel;
import com.anonym.common.anno.AdminNoValidPrivilege;
import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.employee.EmployeeResponseCodeConst;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeLoginFormDTO;
import com.anonym.module.admin.employee.basic.domain.dto.KaptchaVO;
import com.anonym.module.admin.employee.login.domain.EmployeeLoginDetailVO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 后台登录
 */
@RestController
@Api(tags = {SwaggerTagConst.Admin.MANAGER_EMPLOYEE_LOGIN})
@OperateLog
public class EmployeeLoginController {

    @Autowired
    private EmployeeLoginService loginService;

    @PostMapping("/admin/login")
    @ApiOperation(value = "登录", notes = "登录")
    @AdminAuthorityLevel
    public ResponseDTO<EmployeeLoginDetailVO> login(@Valid @RequestBody EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
        return loginService.login(loginForm, request);
    }

    @GetMapping("/admin/logout")
    @ApiOperation(value = "退出登陆", notes = "退出登陆")
    @AdminNoValidPrivilege
    public ResponseDTO<Boolean> logout() {
        LoginTokenDTO requestToken = SmartRequestTokenUtil.getRequestUser();
        if (null == requestToken) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_ERROR);
        }
        return loginService.logoutByToken(requestToken);
    }

}

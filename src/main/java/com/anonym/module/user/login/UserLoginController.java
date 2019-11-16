package com.anonym.module.user.login;

import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.user.basic.domain.UserLoginVO;
import com.anonym.module.user.login.domain.UserLoginDTO;
import com.anonym.utils.SmartIPUtil;
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
 * 登录业务路由
 */
@Api(tags = SwaggerTagConst.App.LOGIN)
@RestController
public class UserLoginController extends AppBaseController {

    @Autowired
    private UserLoginService userLoginService;

    @AppAuthorityLevel
    @ApiOperation("微信用户登录")
    @PostMapping("/user/login")
    public ResponseDTO<UserLoginVO> login(@RequestBody @Valid UserLoginDTO loginDTO, HttpServletRequest request) {
        String remoteIp = SmartIPUtil.getRemoteIp(request);
        loginDTO.setLastLoginIp(remoteIp);
        return userLoginService.login(loginDTO);
    }

    @ApiOperation("退出登录")
    @GetMapping("/user/logout")
    public ResponseDTO<String> logout() {
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        return userLoginService.logout(userId);
    }

}

package com.anonym.module.user.basic;

import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.user.basic.domain.UserBindWeChatPhone;
import com.anonym.module.user.basic.domain.UserInfoUpdateDTO;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * APP 用户业务 路由
 */
@Api(tags = SwaggerTagConst.App.USER_ACCOUNT)
@RestController
public class UserController extends AppBaseController {

    @Autowired
    private UserService userService;

    @ApiOperation("更换绑定手机号 @author lizongliang")
    @PostMapping("/user/update/bind")
    public ResponseDTO<String> updateUserInfo(@Valid @RequestBody UserInfoUpdateDTO updateDTO) {
        LoginTokenDTO loginTokenDTO = SmartRequestTokenUtil.getRequestUser();
        return userService.updateUserInfo(loginTokenDTO.getId(), updateDTO);
    }

    @ApiOperation("用户绑定微信手机号")
    @PostMapping("/user/update/bind/wechat")
    public ResponseDTO<String> bindWeChatPhone(@Valid @RequestBody UserBindWeChatPhone weChatPhone) {
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        weChatPhone.setUserId(userId);
        return userService.bindWeChatPhone(weChatPhone);
    }
}

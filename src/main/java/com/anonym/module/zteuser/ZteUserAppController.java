package com.anonym.module.zteuser;

import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.user.basic.domain.UserLoginVO;
import com.anonym.module.zteuser.domain.ZteLoginDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-12-27 10:47
 */
@Api(tags = SwaggerTagConst.App.ZTE_USER)
@RestController
public class ZteUserAppController extends AppBaseController {

    @Autowired
    private ZteUserService zteUserService;


    @AppAuthorityLevel
    @ApiOperation("中兴用户登录")
    @PostMapping("/zteUser/login")
    public ResponseDTO<UserLoginVO> login(@RequestBody @Valid ZteLoginDTO loginDTO) {
        return zteUserService.login(loginDTO);
    }


}

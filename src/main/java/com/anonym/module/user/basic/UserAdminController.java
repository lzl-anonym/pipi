package com.anonym.module.user.basic;

import com.anonym.common.controller.AdminBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.user.basic.domain.UserAdminQueryDTO;
import com.anonym.module.user.basic.domain.UserAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 后管 管理 用户业务 路由
 */
@Api(tags = SwaggerTagConst.Admin.MANAGER_USER)
@RestController
public class UserAdminController extends AdminBaseController {

    @Autowired
    private UserAdminService userAdminService;

    @ApiOperation("后管分页查询用户列表")
    @PostMapping("/user/query")
    public ResponseDTO<PageResultDTO<UserAdminVO>> query(@Valid @RequestBody UserAdminQueryDTO query) {
        return userAdminService.query(query);
    }

    @ApiOperation("更新用户禁用状态")
    @GetMapping("/user/update/disabled/{userId}/{disabled}")
    public ResponseDTO<String> updateDisabled(@PathVariable Long userId, @PathVariable Boolean disabled) {
        return userAdminService.updateDisabled(userId, disabled);
    }
}

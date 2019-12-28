package com.anonym.module.fuseuser;

import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.fuseuser.fuse.domian.FaceFuseAddDTO;
import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.vo.MessageAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-11-16 14:51
 */
@Api(tags = SwaggerTagConst.App.FACE_FUSE)
@RestController
public class FaceFuseAPPController extends AppBaseController {

    @Autowired
    private FaceFuseService faceFuseService;


//    @AppAuthorityLevel
//    @ApiOperation("添加用户(融合业务)")
//    @PostMapping("/faceFuse/user/add")
//    public ResponseDTO addFaceFuseUser(@RequestBody @Valid FaceFuseUserAddDTO addDTO) {
//        return faceFuseService.addFaceFuseUser(addDTO);
//    }

//    @AppAuthorityLevel
//    @ApiOperation("登录(融合业务)")
//    @GetMapping("/faceFuse/user/login/{phone}")
//    public ResponseDTO<UserLoginVO> faceFuseLogin(@PathVariable String phone) {
//        return faceFuseService.faceFuseLogin(phone);
//    }

    @AppAuthorityLevel
    @PostMapping("/faceFuse/save")
    @ApiOperation("合成 @author lizongliang")
    public ResponseDTO<String> addFaceFuse(@RequestBody @Valid FaceFuseAddDTO addDTO) {
//        Long userId = SmartRequestTokenUtil.getRequestUserId();
        return faceFuseService.addFaceFuse(addDTO, 666L);
    }



}

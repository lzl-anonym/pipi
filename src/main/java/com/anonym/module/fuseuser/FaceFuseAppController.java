package com.anonym.module.fuseuser;

import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.fuseuser.fuse.domian.FaceFuseAddDTO;
import com.anonym.utils.SmartRequestTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-11-16 14:51
 */
@Api(tags = SwaggerTagConst.App.FACE_FUSE)
@RestController
public class FaceFuseAppController extends AppBaseController {

    @Autowired
    private FaceFuseService faceFuseService;

    @PostMapping("/faceFuse/save")
    @ApiOperation("合成 @author lizongliang")
    public ResponseDTO<String> addFaceFuse(@RequestBody @Valid FaceFuseAddDTO addDTO) {
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        return faceFuseService.addFaceFuse(addDTO, userId);
    }


}

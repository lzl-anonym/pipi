package com.anonym.module.face;

import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizongliang
 * @date 2019-11-15 17:32
 */
@Api(tags = SwaggerTagConst.App.FACE_FUSE)
@RestController
public class FaceFuseController extends AppBaseController {

    @Autowired
    private FaceFuseService faceFuseService;

    @AppAuthorityLevel
    @PostMapping("/faceFuse/token/query")
    @ApiOperation("新增周边 @author lizongliang")
    public ResponseDTO<String> addSurrounding() {
        return faceFuseService.faceMerge();
    }
}

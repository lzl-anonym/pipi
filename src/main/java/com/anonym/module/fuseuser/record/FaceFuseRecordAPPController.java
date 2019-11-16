package com.anonym.module.fuseuser.record;

import com.anonym.common.controller.AppBaseController;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizongliang
 * @date 2019-11-16 16:02
 */
@Api(tags = SwaggerTagConst.App.FACE_FUSE_RECORD)
@RestController
public class FaceFuseRecordAPPController extends AppBaseController {

    @Autowired
    private FaceFuseRecordService faceFuseRecordService;


    @ApiOperation("添加参与记录(融合业务)")
    @GetMapping("/faceFuseRecord/add")
    public ResponseDTO addRecord() {
        return faceFuseRecordService.addRecord();
    }


}

package com.anonym.module.fuseuser.record;

import com.anonym.common.controller.AdminBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordQueryDTO;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-11-16 16:21
 */
@Api(tags = SwaggerTagConst.Admin.FACE_FUSE_RECORD)
@RestController
public class FaceFuseRecordAdminController extends AdminBaseController {


    @Autowired
    private FaceFuseRecordService faceFuseRecordService;

    @PostMapping("/faceFuseRecord/query")
    @ApiOperation("分页查询参与记录 @author lizongliang")
    public ResponseDTO<PageResultDTO<FaceFuseRecordVO>> queryByPage(@Valid @RequestBody FaceFuseRecordQueryDTO queryDTO) {
        return faceFuseRecordService.queryByPage(queryDTO);
    }


}

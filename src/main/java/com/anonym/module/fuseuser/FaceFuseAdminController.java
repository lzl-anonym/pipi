package com.anonym.module.fuseuser;

import com.anonym.common.anno.AdminAuthorityLevel;
import com.anonym.common.controller.AdminBaseController;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.fuseuser.domain.FaceFuseQueryDTO;
import com.anonym.module.fuseuser.fuse.domian.FaceFuseDeleteDTO;
import com.anonym.module.fuseuser.fuse.domian.FaceFuseVO;
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
@Api(tags = SwaggerTagConst.Admin.FACE_FUSE)
@RestController
public class FaceFuseAdminController extends AdminBaseController {

	@Autowired
	private FaceFuseService faceFuseService;


	@AdminAuthorityLevel
	@PostMapping("/faceFuse/query")
	@ApiOperation("分页查询融合活动结果 @author lizongliang")
	public ResponseDTO<PageResultDTO<FaceFuseVO>> queryByPage(@Valid @RequestBody FaceFuseQueryDTO queryDTO) {
		return faceFuseService.queryByPage(queryDTO);
	}


	@AdminAuthorityLevel
	@ApiOperation("批量删除融合结果 @author lizongliang")
	@PostMapping("/faceFuse/batchDelete")
	public ResponseDTO<String> batchDelete(@Valid @RequestBody FaceFuseDeleteDTO deleteDTO) {
		return faceFuseService.batchDelete(deleteDTO);
	}


}

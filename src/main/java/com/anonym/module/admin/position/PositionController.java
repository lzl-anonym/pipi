package com.anonym.module.admin.position;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.admin.position.domain.dto.PositionAddDTO;
import com.anonym.module.admin.position.domain.dto.PositionQueryDTO;
import com.anonym.module.admin.position.domain.dto.PositionResultVO;
import com.anonym.module.admin.position.domain.dto.PositionUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = {SwaggerTagConst.Admin.MANAGER_JOB})
@OperateLog
@RestController
public class PositionController {

    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "分页查询所有岗位")
    @PostMapping("/admin/position/getListPage")
    public ResponseDTO<PageResultDTO<PositionResultVO>> getJobPage(@RequestBody @Valid PositionQueryDTO queryDTO) {
        return positionService.queryPositionByPage(queryDTO);
    }

    @ApiOperation(value = "添加岗位")
    @PostMapping("/admin/position/add")
    public ResponseDTO<String> addJob(@RequestBody @Valid PositionAddDTO addDTO) {
        return positionService.addPosition(addDTO);
    }

    @ApiOperation(value = "更新岗位")
    @PostMapping("/admin/position/update")
    public ResponseDTO<String> updateJob(@RequestBody @Valid PositionUpdateDTO updateDTO) {
        return positionService.updatePosition(updateDTO);
    }

    @ApiOperation(value = "根据ID查询岗位")
    @GetMapping("/admin/position/queryById/{id}")
    public ResponseDTO<PositionResultVO> queryJobById(@PathVariable Long id) {
        return positionService.queryPositionById(id);
    }

    @ApiOperation(value = "根据ID删除岗位")
    @GetMapping("/admin/position/remove/{id}")
    public ResponseDTO<String> removeJob(@PathVariable Long id) {
        return positionService.removePosition(id);
    }

}

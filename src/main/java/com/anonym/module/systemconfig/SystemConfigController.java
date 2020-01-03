package com.anonym.module.systemconfig;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.SwaggerTagConst;
import com.anonym.module.systemconfig.domain.dto.SystemConfigAddDTO;
import com.anonym.module.systemconfig.domain.dto.SystemConfigQueryDTO;
import com.anonym.module.systemconfig.domain.dto.SystemConfigUpdateDTO;
import com.anonym.module.systemconfig.domain.dto.SystemConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Api(tags = {SwaggerTagConst.Admin.MANAGER_SYSTEM_CONFIG})
@OperateLog
@RestController
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @ApiOperation(value = "分页查询所有系统配置", notes = "分页查询所有系统配置")
    @PostMapping("/admin/systemConfig/getListPage")
    public ResponseDTO<PageResultDTO<SystemConfigVO>> getSystemConfigPage(@RequestBody @Valid SystemConfigQueryDTO queryDTO) {
        return systemConfigService.getSystemConfigPage(queryDTO);
    }




    @ApiOperation(value = "根据分组查询所有系统配置", notes = "根据分组查询所有系统配置")
    @GetMapping("/admin/systemConfig/getListByGroup")
    public ResponseDTO<List<SystemConfigVO>> getListByGroup(String group) {
        return systemConfigService.getListByGroup(group);
    }

    @ApiOperation(value = "通过key获取对应的信息", notes = "通过key获取对应的信息")
    @GetMapping("/admin/systemConfig/selectByKey")
    public ResponseDTO<SystemConfigVO> selectByKey(String configKey) {
        return systemConfigService.selectByKey(configKey);
    }

}

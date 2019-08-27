package com.anonym.module.systemconfig;

import com.anonym.common.anno.OperateLog;
import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.systemconfig.domain.SystemConfigAddDTO;
import com.anonym.module.systemconfig.domain.SystemConfigDTO;
import com.anonym.module.systemconfig.domain.SystemConfigQueryDTO;
import com.anonym.module.systemconfig.domain.SystemConfigUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@Api(tags = {SwaggerTagConst.MANAGER_SYSTEM_CONFIG})
@OperateLog
@RestController
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @ApiOperation(value = "分页查询所有系统配置", notes = "分页查询所有系统配置")
    @PostMapping("systemConfig/getListPage")
    public ResponseDTO<PageInfoDTO<SystemConfigDTO>> getSystemConfigPage(@RequestBody @Valid SystemConfigQueryDTO queryDTO) {
        return systemConfigService.getSystemConfigPage(queryDTO);
    }

    @ApiOperation(value = "添加配置参数", notes = "添加配置参数")
    @PostMapping("systemConfig/add")
    public ResponseDTO<String> addSystemConfig(@RequestBody @Valid SystemConfigAddDTO configAddDTO) {
        return systemConfigService.addSystemConfig(configAddDTO);
    }

    @ApiOperation(value = "修改配置参数", notes = "修改配置参数")
    @PostMapping("systemConfig/update")
    public ResponseDTO<String> updateSystemConfig(@RequestBody @Valid SystemConfigUpdateDTO updateDTO) {
        return systemConfigService.updateSystemConfig(updateDTO);
    }

    @ApiOperation(value = "根据分组查询所有系统配置", notes = "根据分组查询所有系统配置")
    @GetMapping("systemConfig/getListByGroup")
    public ResponseDTO<List<SystemConfigDTO>> getListByGroup(String group) {
        return systemConfigService.getListByGroup(group);
    }

    @ApiOperation(value = "通过key获取对应的信息", notes = "通过key获取对应的信息")
    @GetMapping("systemConfig/selectByKey")
    public ResponseDTO<SystemConfigDTO> selectByKey(String configKey) {
        return systemConfigService.selectByKey(configKey);
    }

}

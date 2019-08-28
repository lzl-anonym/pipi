package com.anonym.module.serviceitem;

import com.anonym.common.constant.SwaggerTagConst;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.serviceitem.domian.ServiceItemAddDTO;
import com.anonym.module.serviceitem.domian.ServiceItemQueryDTO;
import com.anonym.module.serviceitem.domian.ServiceItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author lizongliang
 * @date 2019-08-28 20:54
 */
@RestController
@Api(tags = {SwaggerTagConst.SERVICE_ITEM})
public class ServiceItemController {

    @Autowired
    private ServiceItemService serviceItemService;

    @ApiOperation(value = "分页查询服务项", notes = "@author lizongliang")
    @PostMapping("/serviceItem/page/query")
    public ResponseDTO<PageInfoDTO<ServiceItemVO>> queryByPage(@RequestBody ServiceItemQueryDTO queryDTO) {
        return serviceItemService.queryByPage(queryDTO);
    }

    @ApiOperation(value = "添加服务项", notes = "@author lizongliang")
    @PostMapping("/serviceItem/add")
    public ResponseDTO<String> add(@RequestBody @Valid ServiceItemAddDTO addTO) {
        return serviceItemService.add(addTO);
    }


}

package com.anonym.module.serviceitem;

import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.serviceitem.domian.*;
import com.anonym.utils.PaginationUtil;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author lizongliang
 * @date 2019-08-28 20:51
 */
@Service
public class ServiceItemService {

    @Autowired
    private ServiceItemDao serviceItemDao;


    /**
     * 分页查询服务项
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageInfoDTO<ServiceItemVO>> queryByPage(ServiceItemQueryDTO queryDTO) {
        Page page = PaginationUtil.convert2PageQueryInfo(queryDTO);
        List<ServiceItemVO> entities = serviceItemDao.queryByPage(page, queryDTO);
        PageInfoDTO<ServiceItemVO> pageInfoDTO = PaginationUtil.convert2PageInfoDTO(page, entities);
        return ResponseDTO.succData(pageInfoDTO);
    }

    /**
     * 添加服务项
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> add(ServiceItemAddDTO addDTO) {
        ServiceItemEntity byName = serviceItemDao.getByName(addDTO.getItemName(), null);
        if (Objects.nonNull(byName)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.ITEM_NAME_EXISTS);
        }
        ServiceItemEntity serviceItemEntity = SmartBeanUtil.copy(addDTO, ServiceItemEntity.class);
        serviceItemDao.insert(serviceItemEntity);
        return ResponseDTO.succ();
    }

    /**
     * 修改服务项
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> update(ServiceItemUpdateDTO updateDTO) {
        ServiceItemEntity byName = serviceItemDao.getByName(updateDTO.getItemName(), updateDTO.getServiceItemId());
        if (Objects.nonNull(byName)) {
            return ResponseDTO.wrap(ServiceItemResponseCodeConst.ITEM_NAME_EXISTS);
        }
        ServiceItemEntity entity = SmartBeanUtil.copy(updateDTO, ServiceItemEntity.class);
        serviceItemDao.updateById(entity);
        return ResponseDTO.succ();
    }

    // TODO: 2019-08-28 删除时判断有没有用户使用


}

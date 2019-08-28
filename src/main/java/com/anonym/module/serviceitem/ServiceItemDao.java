package com.anonym.module.serviceitem;

import com.anonym.module.serviceitem.domian.ServiceItemEntity;
import com.anonym.module.serviceitem.domian.ServiceItemQueryDTO;
import com.anonym.module.serviceitem.domian.ServiceItemVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-08-28 20:37
 */
@Mapper
@Component
public interface ServiceItemDao extends BaseMapper<ServiceItemEntity> {


    /**
     * 分页查询服务项
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<ServiceItemVO> queryByPage(Page page, @Param("queryDTO") ServiceItemQueryDTO queryDTO);

    /**
     * 根据服务名查询
     *
     * @param name
     * @param excludeId
     * @return
     */
    ServiceItemEntity getByName(@Param("name") String name, @Param("excludeId") Integer excludeId);
}

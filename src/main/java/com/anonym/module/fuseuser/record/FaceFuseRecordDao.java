package com.anonym.module.fuseuser.record;

import com.anonym.module.fuseuser.record.domain.FaceFuseRecordEntity;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordQueryDTO;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface FaceFuseRecordDao extends BaseMapper<FaceFuseRecordEntity> {

    /**
     * 分页查询参与记录
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<FaceFuseRecordVO> queryByPage(Page page, @Param("queryDTO") FaceFuseRecordQueryDTO queryDTO);
}

package com.anonym.module.admin.position;

import com.anonym.module.admin.position.domain.dto.PositionQueryDTO;
import com.anonym.module.admin.position.domain.dto.PositionRelationAddDTO;
import com.anonym.module.admin.position.domain.dto.PositionRelationQueryDTO;
import com.anonym.module.admin.position.domain.dto.PositionRelationResultDTO;
import com.anonym.module.admin.position.domain.entity.PositionEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface PositionDao extends BaseMapper<PositionEntity> {

    /**
     * 查询岗位列表
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<PositionEntity> selectByPage(Pagination page, PositionQueryDTO queryDTO);

    /**
     * 查询岗位与人员关系
     *
     * @param jobRelationQueryDTO
     * @return
     */
    List<PositionRelationResultDTO> selectRelation(PositionRelationQueryDTO jobRelationQueryDTO);

    /**
     * 批量添加岗位 人员 关联关系
     *
     * @param jobRelAddDTO
     * @return
     */
    Integer insertBatchRelation(@Param("batchDTO") PositionRelationAddDTO jobRelAddDTO);

    /**
     * 删除指定人员的 岗位关联关系
     *
     * @param employeeId
     * @return
     */
    Integer deleteRelationByEmployeeId(@Param("employeeId") Long employeeId);

}

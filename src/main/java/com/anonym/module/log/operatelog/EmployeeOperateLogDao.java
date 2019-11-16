package com.anonym.module.log.operatelog;

import com.anonym.module.log.operatelog.domain.EmployeeOperateLogEntity;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogQueryDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface EmployeeOperateLogDao extends BaseMapper<EmployeeOperateLogEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return UserOperateLogEntity
     */
    List<EmployeeOperateLogEntity> queryByPage(Pagination page, @Param("queryDTO") EmployeeOperateLogQueryDTO queryDTO);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    void deleteByIds(@Param("idList") List<Long> idList);
}

package com.anonym.module.log.loginlog;

import com.anonym.module.log.loginlog.domain.EmployeeLoginLogEntity;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogQueryDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface EmployeeLoginLogDao extends BaseMapper<EmployeeLoginLogEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return UserLoginLogEntity
     */
    List<EmployeeLoginLogEntity> queryByPage(Pagination page, @Param("queryDTO") EmployeeLoginLogQueryDTO queryDTO);

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

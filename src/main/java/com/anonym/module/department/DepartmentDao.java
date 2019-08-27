package com.anonym.module.department;

import com.anonym.module.department.domain.DepartmentDTO;
import com.anonym.module.department.domain.DepartmentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_department dao接口
 */
@Component
@Mapper
public interface DepartmentDao extends BaseMapper<DepartmentEntity> {

    /**
     * 根据部门id，查询此部门子部门的数量
     *
     * @param departmentId
     * @return int 子部门的数量
     */
    Integer countSubDepartment(@Param("departmentId") Long departmentId);

    /**
     * 获取全部部门列表
     *
     * @return List<DepartmentDTO>
     */
    List<DepartmentDTO> listAll();

    /**
     * 获取全部部门列表(未删除 )
     *
     * @return
     */
    List<DepartmentDTO> queryDepartmentList();

    /**
     * 功能描述: 根据父部门id查询
     *
     * @param
     * @return
     */
    List<DepartmentDTO> selectByParentId(@Param("departmentId") Long departmentId);

    /**
     * 批量新添部门
     */
    Integer batchInsert(@Param("list") List<DepartmentEntity> departmentEntities);

    /**
     * 查询部门排序
     */
    Integer selectMaxSort();

    /**
     * 批量更新部门
     */
    Integer batchUpdate(@Param("list") List<DepartmentEntity> departmentEntities);

}

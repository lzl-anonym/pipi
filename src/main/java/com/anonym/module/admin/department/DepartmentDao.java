package com.anonym.module.admin.department;

import com.anonym.module.admin.department.domain.dto.DepartmentVO;
import com.anonym.module.admin.department.domain.entity.DepartmentEntity;
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
     * @return List<DepartmentVO>
     */
    List<DepartmentVO> listAll();

    /**
     * 功能描述: 根据父部门id查询
     *
     * @param
     * @return
     * @auther yandanyang
     * @date 2018/8/25 0025 上午 9:46
     */
    List<DepartmentVO> selectByParentId(@Param("departmentId") Long departmentId);


    /**
     * 根据部门名称查询部门
     *
     * @param departmentName
     * @return
     */
    DepartmentEntity selectByDepartmentName(@Param("departmentName") String departmentName);

}

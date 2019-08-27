package com.anonym.module.roleemployee;

import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.role.domain.RoleQueryDTO;
import com.anonym.module.roleemployee.domain.RoleEmployeeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface RoleEmployeeDao extends BaseMapper<RoleEmployeeEntity> {

    /**
     * 根据员工id 查询所有的角色
     *
     * @param employeeId
     * @return
     */
    List<Integer> selectRoleIdByEmployeeId(@Param("employeeId") Integer employeeId);

    List<EmployeeDTO> selectEmployeeByNamePage(Page page, @Param("queryDTO") RoleQueryDTO queryDTO);

    List<EmployeeDTO> selectEmployeeByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据员工信息删除
     *
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Integer employeeId);

    /**
     * 删除某个角色的所有关系
     *
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * 根据员工和 角色删除关系
     *
     * @param employeeId
     * @param roleId
     */
    void deleteByEmployeeIdRoleId(@Param("employeeId") Integer employeeId, @Param("roleId") Integer roleId);

    /**
     * 批量删除某个角色下的某批用户的关联关系
     *
     * @param roleId
     * @param employeeIds
     */
    void batchDeleteEmployeeRole(@Param("roleId") Integer roleId, @Param("employeeIds") List<Integer> employeeIds);

    /**
     * 批量新增
     *
     * @param roleRelationList
     */
    void batchInsert(List<RoleEmployeeEntity> roleRelationList);
}

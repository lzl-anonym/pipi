package com.anonym.module.admin.role.roleemployee;

import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.role.basic.domain.dto.RoleQueryDTO;
import com.anonym.module.admin.role.roleemployee.domain.RoleEmployeeEntity;
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
    List<Long> selectRoleIdByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * @param page
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeByNamePage(Page page, @Param("queryDTO") RoleQueryDTO queryDTO);

    /**
     * @param roleId
     * @return
     */
    List<EmployeeDTO> selectEmployeeByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据员工信息删除
     *
     * @param employeeId
     */
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 删除某个角色的所有关系
     *
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据员工和 角色删除关系
     *
     * @param employeeId
     * @param roleId
     */
    void deleteByEmployeeIdRoleId(@Param("employeeId") Long employeeId, @Param("roleId") Long roleId);

    /**
     * 批量删除某个角色下的某批用户的关联关系
     *
     * @param roleId
     * @param employeeIds
     */
    void batchDeleteEmployeeRole(@Param("roleId") Long roleId, @Param("employeeIds") List<Long> employeeIds);

    /**
     * 批量新增
     *
     * @param roleRelationList
     */
    void batchInsert(List<RoleEmployeeEntity> roleRelationList);
}

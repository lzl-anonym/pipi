package com.anonym.module.employee;


import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.employee.domain.EmployeeEntity;
import com.anonym.module.employee.domain.EmployeeQueryDTO;
import com.anonym.module.employee.domain.EmployeeQueryExportDTO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {

    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    EmployeeDTO login(@Param("loginName") String loginName, @Param("loginPwd") String loginPwd);

    /**
     * 查询员工列表
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeList(Page page, @Param("queryDTO") EmployeeQueryDTO queryDTO);

    /**
     * 不带分页查询员工列表
     *
     * @param queryDTO
     * @return
     */
    List<EmployeeDTO> selectEmployeeList(@Param("queryDTO") EmployeeQueryExportDTO queryDTO);

    /**
     * 批量更新禁用状态
     *
     * @param employeeIds
     * @param isDisabled
     */
    void batchUpdateStatus(@Param("employeeIds") List<Long> employeeIds, @Param("isDisabled") Integer isDisabled);

    /**
     * 通过登录名查询
     *
     * @param loginName
     * @param isDisabled
     * @return
     */
    EmployeeDTO getByLoginName(@Param("loginName") String loginName, @Param("isDisabled") Integer isDisabled);

    /**
     * 通过手机号查询
     *
     * @param phone
     * @param isDisabled
     * @return
     */
    EmployeeDTO getByPhone(@Param("phone") String phone, @Param("isDisabled") Integer isDisabled);

    /**
     * 获取所有员工
     *
     * @return
     */
    List<EmployeeDTO> listAll();

    /**
     * 获取某个部门员工数
     *
     * @param departmentId
     * @return
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 获取一批员工
     *
     * @param employeeIds
     * @return
     */
    List<EmployeeDTO> getEmployeeByIds(@Param("ids") Collection<Long> employeeIds);

    /**
     * 获取某个部门的员工
     *
     * @param departmentId
     * @return
     */
    List<EmployeeDTO> getEmployeeIdByDeptId(@Param("departmentId") Long departmentId);

    /**
     * 获取某批部门的员工
     *
     * @param departmentIds
     * @return
     */
    List<EmployeeDTO> getEmployeeIdByDeptIds(@Param("departmentIds") List<Long> departmentIds);

    /**
     * 员工重置密码
     *
     * @param employeeId
     * @param password
     * @return
     */
    Integer updatePassword(@Param("employeeId") Integer employeeId, @Param("password") String password);

    /**
     * 根据岗位查询员工
     *
     * @param positionId
     * @return
     */
    List<EmployeeDTO> getByPositionId(Integer positionId);

    /**
     * 根据 员工编号 批量查询员工数据
     *
     * @param numberList
     * @return
     */
    List<EmployeeEntity> listByEmployeeNumber(List<String> numberList);

    /**
     * 批量新添员工
     */
    Integer batchInsert(@Param("list") List<EmployeeEntity> list);

    /**
     * 批量更新员工
     */
    Integer batchUpdate(@Param("list") List<EmployeeEntity> list);

    /**
     * 根据登录账号集合查询员工
     */
    List<EmployeeEntity> selectListByLoginNameList(@Param("list") List<String> phoneList, @Param("status") Integer status);


}

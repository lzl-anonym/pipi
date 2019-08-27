package com.anonym.module.roleemployee;

import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.department.DepartmentDao;
import com.anonym.module.department.domain.DepartmentEntity;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.role.RoleDao;
import com.anonym.module.role.RoleResponseCodeConst;
import com.anonym.module.role.domain.RoleBatchDTO;
import com.anonym.module.role.domain.RoleDTO;
import com.anonym.module.role.domain.RoleQueryDTO;
import com.anonym.module.role.domain.RoleSelectedDTO;
import com.anonym.module.roleemployee.domain.RoleEmployeeEntity;
import com.anonym.utils.PaginationUtil;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务
 *
 * @date 2019/4/3
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageInfoDTO<EmployeeDTO>> listEmployeeByName(RoleQueryDTO queryDTO) {
        Page page = PaginationUtil.convert2PageQueryInfo(queryDTO);
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByNamePage(page, queryDTO);
        employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).forEach(employeeDTO -> {
            DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
            employeeDTO.setDepartmentName(departmentEntity.getName());
        });
        page.setRecords(employeeDTOS);
        PageInfoDTO<EmployeeDTO> pageInfoDTO = PaginationUtil.convert2PageInfoDTO(page);
        return ResponseDTO.succData(pageInfoDTO);
    }

    public ResponseDTO<List<EmployeeDTO>> getAllEmployeeByRoleId(Integer roleId) {
        List<EmployeeDTO> employeeDTOS = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        return ResponseDTO.succData(employeeDTOS);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeEmployeeRole(Integer employeeId, Integer roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ERROR_PARAM);
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        return ResponseDTO.succ();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param removeDTO
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchRemoveEmployeeRole(RoleBatchDTO removeDTO) {
        List<Integer> employeeIdList = removeDTO.getEmployeeIds();
        roleEmployeeDao.batchDeleteEmployeeRole(removeDTO.getRoleId(), employeeIdList);
        return ResponseDTO.succ();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param addDTO
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> batchAddEmployeeRole(RoleBatchDTO addDTO) {
        Integer roleId = addDTO.getRoleId();
        List<Integer> employeeIdList = addDTO.getEmployeeIds();
        roleEmployeeDao.deleteByRoleId(roleId);
        List<RoleEmployeeEntity> roleRelationEntities = Lists.newArrayList();
        RoleEmployeeEntity employeeRoleRelationEntity;
        for (Integer employeeId : employeeIdList) {
            employeeRoleRelationEntity = new RoleEmployeeEntity();
            employeeRoleRelationEntity.setRoleId(roleId);
            employeeRoleRelationEntity.setEmployeeId(employeeId);
            roleRelationEntities.add(employeeRoleRelationEntity);
        }
        roleEmployeeDao.batchInsert(roleRelationEntities);
        return ResponseDTO.succ();
    }

    /**
     * 通过员工id获取员工角色
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<List<RoleSelectedDTO>> getRolesByEmployeeId(Integer employeeId) {
        List<Integer> roleIds = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleDTO> roleList = roleDao.selectList(new EntityWrapper());
        List<RoleSelectedDTO> result = SmartBeanUtil.copyList(roleList, RoleSelectedDTO.class);
        result.stream().forEach(item -> {
            if (roleIds.contains(item.getId())) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        });
        return ResponseDTO.succData(result);
    }
}

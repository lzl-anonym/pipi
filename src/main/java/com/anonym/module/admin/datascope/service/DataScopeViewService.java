package com.anonym.module.admin.datascope.service;

import com.anonym.module.admin.datascope.DataScopeRoleDao;
import com.anonym.module.admin.datascope.constant.DataScopeViewTypeEnum;
import com.anonym.module.admin.datascope.domain.entity.DataScopeRoleEntity;
import com.anonym.module.admin.department.DepartmentTreeService;
import com.anonym.module.admin.employee.basic.EmployeeDao;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.employee.basic.domain.entity.EmployeeEntity;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.admin.role.roleemployee.RoleEmployeeDao;
import com.anonym.module.systemconfig.SystemConfigService;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.domain.dto.SystemConfigDTO;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DataScopeViewService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private DataScopeRoleDao dataScopeRoleDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 获取某人可以查看的所有人员信息
     *
     * @param dataScopeType
     * @param employeeId
     * @return
     */
    public List<Long> getCanViewEmployeeId(Integer dataScopeType, Long employeeId) {
        Integer viewType = this.getEmployeeDataScopeViewType(dataScopeType, employeeId);
        if (DataScopeViewTypeEnum.ME.getType().equals(viewType)) {
            return this.getMeEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT.getType().equals(viewType)) {
            return this.getDepartmentEmployeeIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB.getType().equals(viewType)) {
            return this.getDepartmentAndSubEmployeeIdList(employeeId);
        }
        return Lists.newArrayList();
    }

    public List<Long> getCanViewDepartmentId(Integer dataScopeType, Long employeeId) {
        Integer viewType = this.getEmployeeDataScopeViewType(dataScopeType, employeeId);
        if (DataScopeViewTypeEnum.ME.getType().equals(viewType)) {
            return this.getMeDepartmentIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT.getType().equals(viewType)) {
            return this.getMeDepartmentIdList(employeeId);
        }
        if (DataScopeViewTypeEnum.DEPARTMENT_AND_SUB.getType().equals(viewType)) {
            return this.getDepartmentAndSubIdList(employeeId);
        }
        return Lists.newArrayList();
    }

    private List<Long> getMeDepartmentIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        return Lists.newArrayList(employeeEntity.getDepartmentId());
    }

    private List<Long> getDepartmentAndSubIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<Long> allDepartmentIds = Lists.newArrayList();
        departmentTreeService.buildIdList(employeeEntity.getDepartmentId(), allDepartmentIds);
        return allDepartmentIds;
    }


    private Integer getEmployeeDataScopeViewType(Integer dataScopeType, Long employeeId) {
        if (employeeId == null) {
            return DataScopeViewTypeEnum.ME.getType();
        }
        if (employeeId.equals(this.getSupermanId())) {
            return DataScopeViewTypeEnum.ALL.getType();
        }
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        //未设置角色 默认本人
        if (CollectionUtils.isEmpty(roleIdList)) {
            return DataScopeViewTypeEnum.ME.getType();
        }
        //未设置角色数据范围 默认本人
        List<DataScopeRoleEntity> dataScopeRoleList = dataScopeRoleDao.listByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(dataScopeRoleList)) {
            return DataScopeViewTypeEnum.ME.getType();
        }
        Map<Integer, List<DataScopeRoleEntity>> listMap = dataScopeRoleList.stream().collect(Collectors.groupingBy(DataScopeRoleEntity::getDataScopeType));
        List<DataScopeRoleEntity> viewLevelList = listMap.get(dataScopeType);
        DataScopeRoleEntity maxLevel = viewLevelList.stream().max(Comparator.comparing(e -> DataScopeViewTypeEnum.valueOf(e.getViewType()).getLevel())).get();
        return maxLevel.getViewType();
    }

    private Long getSupermanId() {
        SystemConfigDTO systemConfig = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        Long supermanId = Long.valueOf(systemConfig.getConfigValue());
        return supermanId;
    }

    /**
     * 获取本人相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getMeEmployeeIdList(Long employeeId) {
        return Lists.newArrayList(employeeId);
    }

    /**
     * 获取本部门相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getDepartmentEmployeeIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<EmployeeVO> employeeList = employeeDao.getEmployeeIdByDeptId(employeeEntity.getDepartmentId());
        List<Long> employeeIdList = employeeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        return employeeIdList;
    }

    /**
     * 获取本部门及下属子部门相关 可查看员工id
     *
     * @param employeeId
     * @return
     */
    private List<Long> getDepartmentAndSubEmployeeIdList(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        List<Long> allDepartmentIds = Lists.newArrayList();
        departmentTreeService.buildIdList(employeeEntity.getDepartmentId(), allDepartmentIds);
        List<EmployeeDTO> employeeList = employeeDao.getEmployeeIdByDeptIds(allDepartmentIds);
        List<Long> employeeIdList = employeeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        return employeeIdList;
    }

}

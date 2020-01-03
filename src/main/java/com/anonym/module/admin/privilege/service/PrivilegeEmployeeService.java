package com.anonym.module.admin.privilege.service;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.exception.SmartBusinessException;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.admin.privilege.dao.PrivilegeDao;
import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.anonym.module.admin.role.roleemployee.RoleEmployeeDao;
import com.anonym.module.admin.role.roleprivilege.RolePrivilegeDao;
import com.anonym.module.systemconfig.SystemConfigService;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.domain.dto.SystemConfigDTO;
import com.anonym.utils.SmartStringUtil;
import com.google.common.collect.Lists;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


@Service
public class PrivilegeEmployeeService {


    private ConcurrentMap<Long, Map<String, List<String>>> employeePrivileges = new ConcurrentLinkedHashMap.Builder<Long, Map<String, List<String>>>().maximumWeightedCapacity(1000).build();

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    @Autowired
    private PrivilegeDao privilegeDao;


    public void removeCache(Long employeeId) {
        this.employeePrivileges.remove(employeeId);
    }

    /**
     * 检查某人是否有访问某个方法的权限
     *
     * @param employeeId
     * @param controllerName
     * @param methodName
     * @return
     */
    public Boolean checkEmployeeHavePrivilege(Long employeeId, String controllerName, String methodName) {
        if (StringUtils.isEmpty(controllerName) || StringUtils.isEmpty(methodName)) {
            return false;
        }
        Boolean isSuperman = this.isSuperman(employeeId);
        if (isSuperman) {
            return true;
        }
        Map<String, List<String>> privileges = this.getPrivileges(employeeId);
        List<String> urlList = privileges.get(controllerName.toLowerCase());
        if (CollectionUtils.isEmpty(urlList)) {
            return false;
        }
        return urlList.contains(methodName);
    }

    /**
     * 判断是否为超级管理员
     *
     * @param employeeId
     * @return
     */
    public Boolean isSuperman(Long employeeId) {
        SystemConfigDTO systemConfig = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        if (systemConfig == null) {
            throw new SmartBusinessException("缺少系统配置项[" + SystemConfigEnum.Key.EMPLOYEE_SUPERMAN.name() + "]");
        }
        Long supermanId = Long.valueOf(systemConfig.getConfigValue());
        return supermanId.equals(employeeId);
    }

    /**
     * 根据员工ID 获取 权限信息
     *
     * @param employeeId
     * @return
     */
    public List<PrivilegeEntity> getPrivilegesByEmployeeId(Long employeeId) {
        // 如果是超管的话
        Boolean isSuperman = this.isSuperman(employeeId);
        if (isSuperman) {
            List<PrivilegeEntity> privilegeEntities = privilegeDao.selectAll();
            if (privilegeEntities == null) {
                return Lists.newArrayList();
            }
            return privilegeEntities;
        }
        List<PrivilegeEntity> privilegeEntities = loadPrivilegeFromDb(employeeId);
        this.updateCachePrivilege(employeeId, privilegeEntities);
        return privilegeEntities;
    }

    /**
     * 获取某人所能访问的方法
     *
     * @param employeeId
     * @return
     */
    private Map<String, List<String>> getPrivileges(Long employeeId) {
        Map<String, List<String>> privileges = employeePrivileges.get(employeeId);
        if (privileges != null) {
            return privileges;
        }
        List<PrivilegeEntity> privilegeEntities = this.loadPrivilegeFromDb(employeeId);
        return updateCachePrivilege(employeeId, privilegeEntities);
    }

    private List<PrivilegeEntity> loadPrivilegeFromDb(Long employeeId) {
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        List<PrivilegeEntity> privilegeEntities = rolePrivilegeDao.listByRoleIds(roleIdList, JudgeEnum.YES.getValue());
        if (privilegeEntities != null) {
            return privilegeEntities;
        }
        return Collections.emptyList();
    }

    public Map<String, List<String>> updateCachePrivilege(Long employeeId, List<PrivilegeEntity> privilegeEntities) {
        List<String> privilegeList = new ArrayList<>();
        Map<String, List<String>> privilegeMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(privilegeEntities)) {
            List<List<String>> setList =
                    privilegeEntities.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.POINTS.getValue())).map(PrivilegeEntity::getUrl).collect(Collectors.toList()).stream().map(e -> SmartStringUtil.splitConvertToList(e, ",")).collect(Collectors.toList());
            setList.forEach(privilegeList::addAll);
        }
        privilegeList.forEach(item -> {
            List<String> path = SmartStringUtil.splitConvertToList(item, "\\.");
            String controllerName = path.get(0).toLowerCase();
            String methodName = path.get(1);
            List<String> methodNameList = privilegeMap.get(controllerName);
            if (null == methodNameList) {
                methodNameList = new ArrayList();
            }
            if (!methodNameList.contains(methodName)) {
                methodNameList.add(methodName);
            }
            privilegeMap.put(controllerName, methodNameList);
        });

        employeePrivileges.put(employeeId, privilegeMap);
        return privilegeMap;
    }

    public void updateOnlineEmployeePrivilegeByRoleId(Long roleId) {
        List<EmployeeDTO> roleEmployeeList = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<Long> employeeIdList = roleEmployeeList.stream().map(e -> e.getId()).collect(Collectors.toList());

        for (Long empId : employeePrivileges.keySet()) {
            if (employeeIdList.contains(empId)) {
                getPrivilegesByEmployeeId(empId);
            }
        }
    }
}

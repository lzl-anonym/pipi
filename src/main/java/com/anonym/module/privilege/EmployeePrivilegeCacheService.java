package com.anonym.module.privilege;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.privilege.domain.PrivilegeEntity;
import com.anonym.module.roleemployee.RoleEmployeeDao;
import com.anonym.module.roleprivilege.RolePrivilegeDao;
import com.anonym.module.systemconfig.SystemConfigService;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.domain.SystemConfigDTO;
import com.anonym.utils.SmartCollectionUtil;
import com.anonym.utils.SmartStringUtil;
import com.google.common.collect.Lists;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * [ 后台员工权限缓存方法 ]
 */
@Service
public class EmployeePrivilegeCacheService {

    /**
     * 后台用户权限 缓存
     */
    private ConcurrentMap<Integer, Map<String, List<String>>> employeePrivileges = new ConcurrentLinkedHashMap.Builder<Integer, Map<String, List<String>>>().maximumWeightedCapacity(1000).build();

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private RoleEmployeeDao roleRelationDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    @Autowired
    private PrivilegeDao employeePrivilegeDao;

    private List<Integer> departmentPrincipalsList;

    public void removeCache(Integer employeeId) {
        this.employeePrivileges.remove(employeeId);
    }

    public Boolean checkEmployeeHavePrivilege(Integer employeeId, String controllerName, String methodName) {
        if (StringUtils.isEmpty(controllerName) || StringUtils.isEmpty(methodName)) {
            return false;
        }
        Integer supermanId = this.getSupermanId();
        if (supermanId.equals(employeeId)) {
            return true;
        }
        Map<String, List<String>> privileges = this.getPrivileges(employeeId);
        List<String> urlList = privileges.get(controllerName.toLowerCase());
        if (SmartCollectionUtil.isEmpty(urlList)) {
            return false;
        }
        return urlList.contains(methodName);
    }

    /**
     * 获取超管id
     *
     * @return
     */
    public Integer getSupermanId() {
        SystemConfigDTO systemConfig = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        Integer supermanId = Integer.valueOf(systemConfig.getConfigValue());
        return supermanId;
    }

    /**
     * 根据员工ID 获取 权限信息
     *
     * @param employeeId
     * @return
     */
    public List<PrivilegeEntity> getPrivilegesByEmployeeId(Integer employeeId) {
        // 如果是超管的话
        Integer supermanId = this.getSupermanId();
        if (supermanId.equals(employeeId)) {
            List<PrivilegeEntity> privilegeEntities = employeePrivilegeDao.selectAll();
            if (CollectionUtils.isNotEmpty(privilegeEntities)) {
                return privilegeEntities.stream().filter(e -> JudgeEnum.YES.getValue().equals(e.getIsEnable())).collect(Collectors.toList());
            }
        }
        List<PrivilegeEntity> privilegeEntities = loadPrivilegeFromDb(employeeId);
        this.updateCachePrivilege(employeeId, privilegeEntities);
        return privilegeEntities;
    }

    private Map<String, List<String>> getPrivileges(Integer employeeId) {
        Map<String, List<String>> privileges = employeePrivileges.get(employeeId);
        if (privileges != null) {
            return privileges;
        }
        List<PrivilegeEntity> privilegeEntities = this.loadPrivilegeFromDb(employeeId);
        return updateCachePrivilege(employeeId, privilegeEntities);
    }

    private List<PrivilegeEntity> loadPrivilegeFromDb(Integer employeeId) {
        List<Integer> roleIdList = roleRelationDao.selectRoleIdByEmployeeId(employeeId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        List<PrivilegeEntity> privilegeEntities = rolePrivilegeDao.listByRoleIds(roleIdList, JudgeEnum.YES.getValue());
        if (privilegeEntities != null) {
            return privilegeEntities.stream().filter(e -> e.getIsEnable().equals(JudgeEnum.YES.getValue())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Map<String, List<String>> updateCachePrivilege(Integer employeeId, List<PrivilegeEntity> privilegeEntities) {
        List<String> privilegeList = new ArrayList<>();
        Map<String, List<String>> privilegeMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(privilegeEntities)) {
            List<List<String>> setList =
                    privilegeEntities.stream().filter(e -> e.getIsEnable() != null && e.getIsEnable().equals(JudgeEnum.YES.getValue())).filter(e -> e.getType().equals(PrivilegeTypeEnum.POINTS.getValue())).map(PrivilegeEntity::getUrl).collect(Collectors.toList()).stream().map(e -> SmartStringUtil.splitConvertToList(e, ",")).collect(Collectors.toList());
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

    /**
     * 根据角色id更新 包含此角色id的在线用户权限
     *
     * @author lidoudou
     * @date 2018/9/19 17:14
     */
    public void updateOnlineEmployeePrivilegeByRoleId(Integer roleId) {
        List<EmployeeDTO> roleEmployeeList = roleRelationDao.selectEmployeeByRoleId(roleId);
        List<Integer> employeeIdList = roleEmployeeList.stream().map(e -> e.getId()).collect(Collectors.toList());

        for (Integer empId : employeePrivileges.keySet()) {
            if (employeeIdList.contains(empId)) {
                getPrivilegesByEmployeeId(empId);
            }
        }
    }
}

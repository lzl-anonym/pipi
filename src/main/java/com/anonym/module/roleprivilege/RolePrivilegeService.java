package com.anonym.module.roleprivilege;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.privilege.EmployeePrivilegeCacheService;
import com.anonym.module.privilege.PrivilegeDao;
import com.anonym.module.privilege.constant.PrivilegeScopeEnum;
import com.anonym.module.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.privilege.domain.PrivilegeEntity;
import com.anonym.module.role.RoleDao;
import com.anonym.module.role.RoleResponseCodeConst;
import com.anonym.module.role.domain.RoleEntity;
import com.anonym.module.roleprivilege.domain.RolePrivilegeDTO;
import com.anonym.module.roleprivilege.domain.RolePrivilegeEntity;
import com.anonym.module.roleprivilege.domain.RolePrivilegeSimpleDTO;
import com.anonym.module.roleprivilege.domain.RolePrivilegeTreeDTO;
import com.anonym.utils.SmartBeanUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [ 后台员工权限 ]
 */
@Service
public class RolePrivilegeService {

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    @Autowired
    private EmployeePrivilegeCacheService employeePrivilegeCacheService;

    /**
     * 更新角色权限
     *
     * @param updateDTO
     * @return ResponseDTO
     */
    public ResponseDTO<String> updateRolePrivilege(RolePrivilegeDTO updateDTO) {

        Integer roleId = updateDTO.getRoleId();
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NOT_EXISTS);
        }
        rolePrivilegeDao.deleteByRoleId(roleId);
        List<RolePrivilegeEntity> rolePrivilegeList = Lists.newArrayList();
        RolePrivilegeEntity rolePrivilegeEntity;
        for (Integer privilegeId : updateDTO.getPrivilegeIdList()) {
            rolePrivilegeEntity = new RolePrivilegeEntity();
            rolePrivilegeEntity.setRoleId(roleId);
            rolePrivilegeEntity.setPrivilegeId(privilegeId);
            rolePrivilegeList.add(rolePrivilegeEntity);
        }
        rolePrivilegeDao.batchInsert(rolePrivilegeList);
        employeePrivilegeCacheService.updateOnlineEmployeePrivilegeByRoleId(roleId);
        return ResponseDTO.succ();
    }

    public ResponseDTO<RolePrivilegeTreeDTO> listPrivilegeByRoleId(Integer roleId) {
        RolePrivilegeTreeDTO rolePrivilegeTreeDTO = new RolePrivilegeTreeDTO();
        rolePrivilegeTreeDTO.setRoleId(roleId);

        List<PrivilegeEntity> privilegeEntityList = privilegeDao.listByEnable(JudgeEnum.YES.getValue(), PrivilegeScopeEnum.BACK.getValue());
        if (CollectionUtils.isEmpty(privilegeEntityList)) {
            rolePrivilegeTreeDTO.setPrivilege(Lists.newArrayList());
            rolePrivilegeTreeDTO.setSelectedIds(Lists.newArrayList());
            return ResponseDTO.succData(rolePrivilegeTreeDTO);
        }

        List<RolePrivilegeSimpleDTO> privilegeRoleSimpleList = SmartBeanUtil.copyList(privilegeEntityList, RolePrivilegeSimpleDTO.class);
        List<RolePrivilegeSimpleDTO> modulePrivilegeList = Lists.newArrayList();
        List<RolePrivilegeSimpleDTO> childPrivilegeList = Lists.newArrayList();
        for (RolePrivilegeSimpleDTO privilegeRoleSimpleDTO : privilegeRoleSimpleList) {
            if (PrivilegeTypeEnum.MODULE.equalsValue(privilegeRoleSimpleDTO.getType())) {
                modulePrivilegeList.add(privilegeRoleSimpleDTO);
                continue;
            }
            if (PrivilegeTypeEnum.PAGE.equalsValue(privilegeRoleSimpleDTO.getType())) {
                childPrivilegeList.add(privilegeRoleSimpleDTO);
                continue;
            }
            if (PrivilegeTypeEnum.POINTS.equalsValue(privilegeRoleSimpleDTO.getType())) {
                childPrivilegeList.add(privilegeRoleSimpleDTO);
                continue;
            }
            if (PrivilegeTypeEnum.CHILDREN_MODULE.equalsValue(privilegeRoleSimpleDTO.getType())) {
                childPrivilegeList.add(privilegeRoleSimpleDTO);
                continue;
            }
        }
        if (CollectionUtils.isEmpty(modulePrivilegeList)) {
            rolePrivilegeTreeDTO.setPrivilege(Lists.newArrayList());
            rolePrivilegeTreeDTO.setSelectedIds(Lists.newArrayList());
            return ResponseDTO.succData(rolePrivilegeTreeDTO);
        }
        //组装模块级权限
        Map<Integer, List<RolePrivilegeSimpleDTO>> pageListMap = childPrivilegeList.stream().collect(Collectors.groupingBy(RolePrivilegeSimpleDTO::getParentId));
        for (RolePrivilegeSimpleDTO privilegeRoleSimpleDTO : modulePrivilegeList) {
            Integer privilegeId = privilegeRoleSimpleDTO.getId();
            //第一层
            List<RolePrivilegeSimpleDTO> privilegeOne = pageListMap.get(privilegeId);
            if (CollectionUtils.isNotEmpty(privilegeOne)) {
                privilegeOne.forEach(e -> {
                    //第二层
                    List<RolePrivilegeSimpleDTO> privilegeTwo = pageListMap.get(e.getId());
                    if (CollectionUtils.isNotEmpty(privilegeTwo)) {
                        privilegeTwo.forEach(i -> {
                            //第三层
                            List<RolePrivilegeSimpleDTO> privilegeThree = pageListMap.get(i.getId());
                            i.setChildren(privilegeThree);
                        });
                        e.setChildren(privilegeTwo);
                    }
                });

                privilegeRoleSimpleDTO.setChildren(privilegeOne);
            }

        }
        //筛选模块、页面、功能点、选中状态
        List<PrivilegeEntity> rolePrivilegeEntityList = rolePrivilegeDao.listByRoleId(roleId);
        List<Integer> privilegeIdList = rolePrivilegeEntityList.stream().map(e -> e.getId()).collect(Collectors.toList());
        rolePrivilegeTreeDTO.setPrivilege(modulePrivilegeList);
        rolePrivilegeTreeDTO.setSelectedIds(privilegeIdList);
        return ResponseDTO.succData(rolePrivilegeTreeDTO);
    }

}

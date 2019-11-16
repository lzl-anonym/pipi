package com.anonym.module.admin.role.roleprivilege;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.admin.privilege.dao.PrivilegeDao;
import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.anonym.module.admin.privilege.service.PrivilegeEmployeeService;
import com.anonym.module.admin.role.basic.RoleDao;
import com.anonym.module.admin.role.basic.RoleResponseCodeConst;
import com.anonym.module.admin.role.basic.domain.entity.RoleEntity;
import com.anonym.module.admin.role.roleprivilege.domain.dto.RolePrivilegeDTO;
import com.anonym.module.admin.role.roleprivilege.domain.dto.RolePrivilegeSimpleDTO;
import com.anonym.module.admin.role.roleprivilege.domain.dto.RolePrivilegeTreeVO;
import com.anonym.module.admin.role.roleprivilege.domain.entity.RolePrivilegeEntity;
import com.anonym.utils.SmartBeanUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
    private PrivilegeEmployeeService privilegeEmployeeService;

    /**
     * 更新角色权限
     *
     * @param updateDTO
     * @return ResponseDTO
     */
    public ResponseDTO<String> updateRolePrivilege(RolePrivilegeDTO updateDTO) {

        Long roleId = updateDTO.getRoleId();
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NOT_EXISTS);
        }
        rolePrivilegeDao.deleteByRoleId(roleId);
        List<RolePrivilegeEntity> rolePrivilegeList = Lists.newArrayList();
        RolePrivilegeEntity rolePrivilegeEntity;
        for (String privilegeKey : updateDTO.getPrivilegeKeyList()) {
            rolePrivilegeEntity = new RolePrivilegeEntity();
            rolePrivilegeEntity.setRoleId(roleId);
            rolePrivilegeEntity.setPrivilegeKey(privilegeKey);
            rolePrivilegeList.add(rolePrivilegeEntity);
        }
        rolePrivilegeDao.batchInsert(rolePrivilegeList);
        privilegeEmployeeService.updateOnlineEmployeePrivilegeByRoleId(roleId);
        return ResponseDTO.succ();
    }

    public ResponseDTO<RolePrivilegeTreeVO> listPrivilegeByRoleId(Long roleId) {
        RolePrivilegeTreeVO rolePrivilegeTreeDTO = new RolePrivilegeTreeVO();
        rolePrivilegeTreeDTO.setRoleId(roleId);

        List<PrivilegeEntity> privilegeDTOList = privilegeDao.selectAll();
        if (CollectionUtils.isEmpty(privilegeDTOList)) {
            rolePrivilegeTreeDTO.setPrivilege(Lists.newArrayList());
            rolePrivilegeTreeDTO.setSelectedKey(Lists.newArrayList());
            return ResponseDTO.succData(rolePrivilegeTreeDTO);
        }
        //构造权限树
        List<RolePrivilegeSimpleDTO> privilegeList = this.buildPrivilegeTree(privilegeDTOList);
        //设置选中状态
        List<PrivilegeEntity> rolePrivilegeEntityList = rolePrivilegeDao.listByRoleId(roleId);
        List<String> privilegeKeyList = rolePrivilegeEntityList.stream().map(e -> e.getKey()).collect(Collectors.toList());
        rolePrivilegeTreeDTO.setPrivilege(privilegeList);
        rolePrivilegeTreeDTO.setSelectedKey(privilegeKeyList);
        return ResponseDTO.succData(rolePrivilegeTreeDTO);
    }

    private List<RolePrivilegeSimpleDTO> buildPrivilegeTree(List<PrivilegeEntity> privilegeEntityList) {
        List<RolePrivilegeSimpleDTO> privilegeTree = Lists.newArrayList();
        List<PrivilegeEntity> rootPrivilege = privilegeEntityList.stream().filter(e -> e.getParentKey() == null).collect(Collectors.toList());
        rootPrivilege.sort(Comparator.comparing(PrivilegeEntity::getSort));
        if (CollectionUtils.isEmpty(rootPrivilege)) {
            return privilegeTree;
        }
        privilegeTree = SmartBeanUtil.copyList(rootPrivilege, RolePrivilegeSimpleDTO.class);
        privilegeTree.forEach(e -> e.setChildren(Lists.newArrayList()));
        this.buildChildPrivilegeList(privilegeEntityList, privilegeTree);
        return privilegeTree;
    }

    private void buildChildPrivilegeList(List<PrivilegeEntity> privilegeEntityList, List<RolePrivilegeSimpleDTO> parentMenuList) {
        List<String> parentKeyList = parentMenuList.stream().map(RolePrivilegeSimpleDTO::getKey).collect(Collectors.toList());
        List<PrivilegeEntity> childEntityList = privilegeEntityList.stream().filter(e -> parentKeyList.contains(e.getParentKey())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childEntityList)) {
            return;
        }
        Map<String, List<PrivilegeEntity>> listMap = childEntityList.stream().collect(Collectors.groupingBy(PrivilegeEntity::getParentKey));
        for (RolePrivilegeSimpleDTO rolePrivilegeSimpleDTO : parentMenuList) {
            String key = rolePrivilegeSimpleDTO.getKey();
            List<PrivilegeEntity> privilegeEntities = listMap.get(key);
            if (CollectionUtils.isEmpty(privilegeEntities)) {
                continue;
            }
            List<RolePrivilegeSimpleDTO> privilegeList = SmartBeanUtil.copyList(privilegeEntities, RolePrivilegeSimpleDTO.class);
            privilegeList.forEach(e -> e.setChildren(Lists.newArrayList()));
            rolePrivilegeSimpleDTO.setChildren(privilegeList);
            this.buildChildPrivilegeList(privilegeEntityList, privilegeList);
        }
    }
}

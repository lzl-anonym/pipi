package com.anonym.module.admin.privilege.service;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.admin.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.admin.privilege.dao.PrivilegeDao;
import com.anonym.module.admin.privilege.domain.dto.*;
import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.anonym.module.admin.role.roleprivilege.RolePrivilegeDao;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRequestUrlService privilegeRequestUrlService;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;


    public ResponseDTO<List<PrivilegeRequestUrlVO>> getPrivilegeUrlDTOList() {
        List<PrivilegeRequestUrlVO> privilegeUrlList = privilegeRequestUrlService.getPrivilegeList();
        return ResponseDTO.succData(privilegeUrlList);
    }

    /**
     * 批量保存权限菜单项
     *
     * @param menuList
     * @return
     */
    public ResponseDTO<String> menuBatchSave(List<PrivilegeMenuDTO> menuList) {
        if (CollectionUtils.isEmpty(menuList)) {
            return ResponseDTO.succ();
        }
        List<PrivilegeEntity> privilegeList = privilegeDao.selectByExcludeType(PrivilegeTypeEnum.POINTS.getValue());
        //若数据库无数据 直接全部保存
        if (CollectionUtils.isEmpty(privilegeList)) {
            List<PrivilegeEntity> menuSaveEntity = this.buildPrivilegeMenuEntity(menuList);
            privilegeDao.batchInsert(menuSaveEntity);
            return ResponseDTO.succ();
        }
        //处理需更新的菜单项
        Map<String, PrivilegeMenuDTO> storageMap = menuList.stream().collect(Collectors.toMap(PrivilegeMenuDTO::getMenuKey, e -> e));
        Set<String> menuKeyList = storageMap.keySet();
        List<PrivilegeEntity> updatePrivilegeList = privilegeList.stream().filter(e -> menuKeyList.contains(e.getKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(updatePrivilegeList)) {
            this.rebuildPrivilegeMenuEntity(storageMap, updatePrivilegeList);
            privilegeDao.batchUpdate(updatePrivilegeList);
        }
        //处理需删除的菜单项
        List<String> delKeyList = privilegeList.stream().filter(e -> !menuKeyList.contains(e.getKey())).map(PrivilegeEntity::getKey).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delKeyList)) {
            privilegeDao.delByParentKeyList(delKeyList);
            //处理需删除的功能点
            privilegeDao.delByParentKeyList(delKeyList);
            rolePrivilegeDao.deleteByPrivilegeKey(delKeyList);
        }

        //处理需新增的菜单项
        List<String> dbKeyList = privilegeList.stream().map(PrivilegeEntity::getKey).collect(Collectors.toList());
        List<PrivilegeMenuDTO> addPrivilegeList = menuList.stream().filter(e -> !dbKeyList.contains(e.getMenuKey())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(addPrivilegeList)) {
            List<PrivilegeEntity> menuAddEntity = this.buildPrivilegeMenuEntity(addPrivilegeList);
            privilegeDao.batchInsert(menuAddEntity);
        }
        return ResponseDTO.succ();
    }

    /**
     * 构建权限菜单项类别
     *
     * @param menuList
     * @return
     */
    private List<PrivilegeEntity> buildPrivilegeMenuEntity(List<PrivilegeMenuDTO> menuList) {
        List<PrivilegeEntity> privilegeList = Lists.newArrayList();
        PrivilegeEntity privilegeEntity;
        for (PrivilegeMenuDTO menuDTO : menuList) {
            privilegeEntity = new PrivilegeEntity();
            privilegeEntity.setKey(menuDTO.getMenuKey());
            privilegeEntity.setName(menuDTO.getMenuName());
            privilegeEntity.setParentKey(menuDTO.getParentKey());
            privilegeEntity.setType(menuDTO.getType());
            privilegeEntity.setSort(menuDTO.getSort());
            privilegeList.add(privilegeEntity);
        }
        return privilegeList;
    }

    /**
     * 更新权限菜单项
     *
     * @param menuMap
     * @param menuEntityList
     */
    private void rebuildPrivilegeMenuEntity(Map<String, PrivilegeMenuDTO> menuMap, List<PrivilegeEntity> menuEntityList) {
        for (PrivilegeEntity menuEntity : menuEntityList) {
            PrivilegeMenuDTO menuDTO = menuMap.get(menuEntity.getKey());
            menuEntity.setName(menuDTO.getMenuName());
            menuEntity.setParentKey(menuDTO.getParentKey());
            menuEntity.setType(menuDTO.getType());
            menuEntity.setSort(menuDTO.getSort());
        }

    }

    /**
     * 查询所有的权限菜单
     *
     * @return
     */
    public ResponseDTO<List<PrivilegeMenuListVO>> menuQueryAll() {
        List<PrivilegeEntity> privilegeEntityList = privilegeDao.selectByType(PrivilegeTypeEnum.MENU.getValue());
        if (CollectionUtils.isEmpty(privilegeEntityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<PrivilegeMenuListVO> menuList = this.buildMenuList(privilegeEntityList);
        return ResponseDTO.succData(menuList);
    }

    private List<PrivilegeMenuListVO> buildMenuList(List<PrivilegeEntity> privilegeEntityList) {
        List<PrivilegeMenuListVO> menuList = Lists.newArrayList();
        List<PrivilegeEntity> rootPrivilege = privilegeEntityList.stream().filter(e -> e.getParentKey() == null).collect(Collectors.toList());
        rootPrivilege.sort(Comparator.comparing(PrivilegeEntity::getSort));
        if (CollectionUtils.isEmpty(rootPrivilege)) {
            return menuList;
        }
        menuList = this.entity2MenuList(rootPrivilege);
        this.buildChildMenuList(privilegeEntityList, menuList);
        return menuList;
    }

    private void buildChildMenuList(List<PrivilegeEntity> privilegeEntityList, List<PrivilegeMenuListVO> parentMenuList) {
        List<String> parentKeyList = parentMenuList.stream().map(PrivilegeMenuListVO::getMenuKey).collect(Collectors.toList());
        List<PrivilegeEntity> childEntityList = privilegeEntityList.stream().filter(e -> parentKeyList.contains(e.getParentKey())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childEntityList)) {
            return;
        }
        Map<String, List<PrivilegeEntity>> listMap = childEntityList.stream().collect(Collectors.groupingBy(PrivilegeEntity::getParentKey));
        for (PrivilegeMenuListVO menuDTO : parentMenuList) {
            String key = menuDTO.getMenuKey();
            List<PrivilegeEntity> privilegeEntities = listMap.get(key);
            if (CollectionUtils.isEmpty(privilegeEntities)) {
                continue;
            }
            privilegeEntities.sort(Comparator.comparing(PrivilegeEntity::getSort));
            List<PrivilegeMenuListVO> menuList = this.entity2MenuList(privilegeEntities);
            menuDTO.setMenuList(menuList);
            this.buildChildMenuList(privilegeEntityList, menuList);
        }
    }

    private List<PrivilegeMenuListVO> entity2MenuList(List<PrivilegeEntity> privilegeEntities) {
        List<PrivilegeMenuListVO> menuList = Lists.newArrayList();
        PrivilegeMenuListVO menuListDTO;
        for (PrivilegeEntity menuEntity : privilegeEntities) {
            menuListDTO = new PrivilegeMenuListVO();
            menuListDTO.setMenuKey(menuEntity.getKey());
            menuListDTO.setMenuParentKey(menuEntity.getParentKey());
            menuListDTO.setMenuName(menuEntity.getName());
            menuListDTO.setMenuList(Lists.newArrayList());
            menuList.add(menuListDTO);
        }
        return menuList;
    }


    public ResponseDTO<String> functionSaveOrUpdate(PrivilegeFunctionDTO privilegeFunctionDTO) {
        String functionKey = privilegeFunctionDTO.getFunctionKey();
        PrivilegeEntity functionEntity = privilegeDao.selectByKey(functionKey);
        if (functionEntity == null) {
            functionEntity = new PrivilegeEntity();
            functionEntity.setName(privilegeFunctionDTO.getFunctionName());
            functionEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
            functionEntity.setType(PrivilegeTypeEnum.POINTS.getValue());
            functionEntity.setUrl(privilegeFunctionDTO.getUrl());
            functionEntity.setKey(privilegeFunctionDTO.getFunctionKey());
            functionEntity.setCreateTime(new Date());
            functionEntity.setUpdateTime(new Date());
            privilegeDao.insert(functionEntity);
        } else {
            functionEntity.setUrl(privilegeFunctionDTO.getUrl());
            functionEntity.setName(privilegeFunctionDTO.getFunctionName());
            functionEntity.setParentKey(privilegeFunctionDTO.getMenuKey());
            privilegeDao.updateById(functionEntity);
        }
        return ResponseDTO.succ();
    }

    /**
     * 查询功能点
     *
     * @param menuKey
     * @return
     */
    public ResponseDTO<List<PrivilegeFunctionVO>> functionQuery(String menuKey) {
        List<PrivilegeEntity> functionPrivilegeList = privilegeDao.selectByParentKey(menuKey);
        if (CollectionUtils.isEmpty(functionPrivilegeList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<PrivilegeFunctionVO> functionList = Lists.newArrayList();
        PrivilegeFunctionVO functionDTO;
        for (PrivilegeEntity functionEntity : functionPrivilegeList) {
            functionDTO = new PrivilegeFunctionVO();
            functionDTO.setFunctionKey(functionEntity.getKey());
            functionDTO.setFunctionName(functionEntity.getName());
            functionDTO.setMenuKey(functionEntity.getParentKey());
            functionDTO.setUrl(functionEntity.getUrl());
            functionList.add(functionDTO);
        }
        return ResponseDTO.succData(functionList);
    }

}

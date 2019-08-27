package com.anonym.module.privilege;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.privilege.constant.PrivilegeResponseCodeConst;
import com.anonym.module.privilege.constant.PrivilegeScopeEnum;
import com.anonym.module.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.privilege.domain.*;
import com.anonym.module.roleprivilege.RolePrivilegeDao;
import com.anonym.utils.SmartBeanUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * [ 后台员工权限 ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 钢圈
 * @copyright (c) 2019 钢圈Inc. All rights reserved.
 * @date
 * @since JDK1.8
 */
@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeCacheService privilegeService;

    @Autowired
    private PrivilegeDao privilegeDao;

    @Autowired
    private RolePrivilegeDao rolePrivilegeDao;

    public List<PrivilegeUrlDTO> getPrivilegeUrlDTOList(Integer scope) {
        List<PrivilegeUrlDTO> privilegeUrlList = Lists.newArrayList();
        if (PrivilegeScopeEnum.BACK.getValue().equals(scope)) {
            privilegeUrlList = privilegeService.getBackPrivilegeList();
        }
        return privilegeUrlList;
    }

    public ResponseDTO<String> insert(PrivilegeAddDTO privilegeAddDTO) {
        if (!PrivilegeTypeEnum.POINTS.getValue().equals(privilegeAddDTO.getType())) {
            List<PrivilegeEntity> privilegeEntityList = privilegeDao.selectByType(PrivilegeTypeEnum.POINTS.getValue());
            List<PrivilegeEntity> keyPrivilegeList = privilegeEntityList.stream().filter(e -> e.getRouterKey().equals(privilegeAddDTO.getRouterKey())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(keyPrivilegeList)) {
                return ResponseDTO.wrap(PrivilegeResponseCodeConst.ROUTER_KEY_NO_REPEAT);
            }
        }
        PrivilegeEntity entity = SmartBeanUtil.copy(privilegeAddDTO, PrivilegeEntity.class);
        entity.setUpdateTime(new Date());
        entity.setCreateTime(new Date());
        privilegeDao.insert(entity);
        return ResponseDTO.succ();
    }

    public ResponseDTO<String> update(PrivilegeUpdateDTO privilegeUpdateDTO) {
        Integer privilegeId = privilegeUpdateDTO.getId();
        PrivilegeEntity privilegeEntity = privilegeDao.selectById(privilegeId);
        if (privilegeUpdateDTO == null) {
            return ResponseDTO.wrap(PrivilegeResponseCodeConst.PRIVILEGE_NOT_EXISTS);
        }

        privilegeEntity = SmartBeanUtil.copy(privilegeUpdateDTO, PrivilegeEntity.class);
        // 初始属性
        privilegeEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        privilegeDao.updateById(privilegeEntity);
        return ResponseDTO.succ();
    }

    /**
     * 删除菜单权限
     * 根据id删除
     *
     * @param privilegeId
     * @return AjaxResult<String>
     */
    public ResponseDTO<String> delPrivilege(Integer privilegeId) {
        List<Integer> delPrivilegeIds = Lists.newArrayList();
        delPrivilegeIds.add(privilegeId);
        getChildrenPrivilege(privilegeId, delPrivilegeIds);
        privilegeDao.delPrivilege(delPrivilegeIds);
        rolePrivilegeDao.deleteByPrivilegeIds(delPrivilegeIds);
        return ResponseDTO.succ();
    }

    /**
     * 查询当前菜单的所有子项
     *
     * @param parentId
     * @param delPrivilegeIds
     */
    private void getChildrenPrivilege(Integer parentId, List<Integer> delPrivilegeIds) {
        List<PrivilegeEntity> selectPrivilegeEntityList = privilegeDao.selectByParentId(parentId);
        if (selectPrivilegeEntityList.size() > 0) {
            selectPrivilegeEntityList.forEach(e -> {
                delPrivilegeIds.add(e.getId());
                getChildrenPrivilege(e.getId(), delPrivilegeIds);
            });
        }
    }

    /**
     * 查询菜单列表
     *
     * @return
     */
    public ResponseDTO<List<PrivilegeDTO>> listPrivilege(Integer scope) {
        // 获取全部菜单列表,排除功能点
        List<PrivilegeEntity> entityList = privilegeDao.selectByExcludeType(PrivilegeTypeEnum.POINTS.getValue(), scope);

        List<PrivilegeDTO> privilegeList = SmartBeanUtil.copyList(entityList, PrivilegeDTO.class);
        // 定义DTO对象集合
        List<PrivilegeDTO> rootPrivilegeList = new ArrayList<>(privilegeList.size());
        Map<Integer, PrivilegeDTO> privilegeMap = new HashMap<>(privilegeList.size());

        // 遍历，换转DTO
        privilegeList.forEach(privilege -> {
            if (privilege.getParentId() == null || privilege.getParentId().equals(0)) {
                rootPrivilegeList.add(privilege);
            }
            privilegeMap.put(privilege.getId(), privilege);
        });

        this.handlePrivilegeList(privilegeList, privilegeMap);

        return ResponseDTO.succData(rootPrivilegeList);
    }

    private void handlePrivilegeList(List<PrivilegeDTO> privilegeList, Map<Integer, PrivilegeDTO> privilegeMap) {
        privilegeList.forEach(privilege -> {
            Integer parentId = privilege.getParentId();
            if (parentId == null || parentId.equals(0)) {
                return;
            }
            // 有父级菜单
            PrivilegeDTO parentPrivilege = privilegeMap.get(parentId);
            if (parentPrivilege == null) {
                return;
            }
            List<PrivilegeDTO> childrenDeptList = parentPrivilege.getChildrenPrivilege();
            if (childrenDeptList == null) {
                childrenDeptList = new ArrayList<>();
                parentPrivilege.setChildrenPrivilege(childrenDeptList);
            }
            childrenDeptList.add(privilege);
        });
    }

    /**
     * 直返回功能点（海帆说的）
     */
    public ResponseDTO<PrivilegeDTO> listPrivilegeById(Integer id) {
        PrivilegeEntity privilegeEntity = privilegeDao.selectById(id);
        if (privilegeEntity == null) {
            return ResponseDTO.wrap(PrivilegeResponseCodeConst.PRIVILEGE_NOT_EXISTS);
        }
        PrivilegeDTO privilegeDTO = SmartBeanUtil.copy(privilegeEntity, PrivilegeDTO.class);
        List<PrivilegeDTO> pages = this.getPagePrivilege(id, privilegeDTO);
        privilegeDTO.setChildrenPages(pages);
        privilegeDTO = getPointPrivilege(id, privilegeDTO);

        return ResponseDTO.succData(privilegeDTO);
    }

    /**
     * 获取菜单下对应的功能点集合
     *
     * @param id
     * @param privilegeDTO
     */
    private PrivilegeDTO getPointPrivilege(Integer id, PrivilegeDTO privilegeDTO) {
        List<PrivilegeDTO> points = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(privilegeDTO.getChildrenPoints())) {
            points = privilegeDTO.getChildrenPoints();
        }

        List<PrivilegeEntity> privilegeEntityPointsList = privilegeDao.selectByParentIdAndType(id, PrivilegeTypeEnum.POINTS.getValue());
        if (CollectionUtils.isNotEmpty(privilegeEntityPointsList)) {
            for (PrivilegeEntity privilege : privilegeEntityPointsList) {
                PrivilegeDTO privilegePointDTO = SmartBeanUtil.copy(privilege, PrivilegeDTO.class);
                points.add(privilegePointDTO);
            }
            privilegeDTO.setChildrenPoints(points);
        }
        return privilegeDTO;
    }

    /**
     * 获取菜单下对应的功能点集合
     *
     * @param id
     * @param privilegeDTO
     * @return
     */
    private List<PrivilegeDTO> getPagePrivilege(Integer id, PrivilegeDTO privilegeDTO) {
        List<PrivilegeDTO> pages = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(privilegeDTO.getChildrenPages())) {
            pages = privilegeDTO.getChildrenPages();
        }
        List<PrivilegeEntity> privilegeEntityPagesList = privilegeDao.selectByParentIdAndType(id, PrivilegeTypeEnum.PAGE.getValue());
        if (CollectionUtils.isNotEmpty(privilegeEntityPagesList)) {
            for (PrivilegeEntity privilege : privilegeEntityPagesList) {
                PrivilegeDTO privilegePageDTO = SmartBeanUtil.copy(privilege, PrivilegeDTO.class);
                pages.add(privilegePageDTO);
            }
            privilegeDTO.setChildrenPages(pages);
        }
        return pages;
    }

}

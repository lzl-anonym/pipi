package com.anonym.module.role;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.role.domain.RoleAddDTO;
import com.anonym.module.role.domain.RoleDTO;
import com.anonym.module.role.domain.RoleEntity;
import com.anonym.module.role.domain.RoleUpdateDTO;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务
 */
@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

//    @Autowired
//    private RolePrivilegeDao rolePrivilegeDao;
//
//    @Autowired
//    private RoleEmployeeDao roleEmployeeDao;

    /**
     * 新增添加角色
     *
     * @param roleAddDTO
     * @return ResponseDTO
     */
    public ResponseDTO addRole(RoleAddDTO roleAddDTO) {
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleAddDTO.getRoleName());
        if (null != employeeRoleEntity) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NAME_EXISTS);
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleAddDTO, RoleEntity.class);
        roleDao.insert(roleEntity);
        return ResponseDTO.succ();
    }


    // TODO: 2019-08-27   添加权限后  放开 
    /*    *//**
     * 根据角色id 删除
     *
     * @param roleId
     * @return ResponseDTO
     *//*
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO deleteRole(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NOT_EXISTS);
        }
        roleDao.deleteById(roleId);
        rolePrivilegeDao.deleteByRoleId(roleId);
        roleEmployeeDao.deleteByRoleId(roleId);
        return ResponseDTO.succ();
    }*/

    /**
     * 更新角色
     *
     * @param roleUpdateDTO
     * @return ResponseDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRole(RoleUpdateDTO roleUpdateDTO) {
        if (null == roleDao.selectById(roleUpdateDTO.getId())) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NOT_EXISTS);
        }
        RoleEntity employeeRoleEntity = roleDao.getByRoleName(roleUpdateDTO.getRoleName());
        if (null != employeeRoleEntity && !employeeRoleEntity.getId().equals(roleUpdateDTO.getId())) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NAME_EXISTS);
        }
        RoleEntity roleEntity = SmartBeanUtil.copy(roleUpdateDTO, RoleEntity.class);
        roleDao.updateById(roleEntity);
        return ResponseDTO.succ();
    }

    /**
     * 根据id获取角色数据
     *
     * @param roleId
     * @return ResponseDTO<RoleDTO>
     */
    public ResponseDTO<RoleDTO> getRoleById(Long roleId) {
        RoleEntity roleEntity = roleDao.selectById(roleId);
        if (null == roleEntity) {
            return ResponseDTO.wrap(RoleResponseCodeConst.ROLE_NOT_EXISTS);
        }
        RoleDTO roleDTO = SmartBeanUtil.copy(roleEntity, RoleDTO.class);
        return ResponseDTO.succData(roleDTO);
    }

    /**
     * 获取所有角色列表
     *
     * @return ResponseDTO
     */
    public ResponseDTO<List<RoleDTO>> getAllRole() {
        List<RoleEntity> roleEntityList = roleDao.selectList(new EntityWrapper());
        List<RoleDTO> roleDTOList = SmartBeanUtil.copyList(roleEntityList, RoleDTO.class);
        return ResponseDTO.succData(roleDTOList);
    }
}

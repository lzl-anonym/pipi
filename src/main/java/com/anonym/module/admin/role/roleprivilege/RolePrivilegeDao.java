package com.anonym.module.admin.role.roleprivilege;

import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.anonym.module.admin.role.roleprivilege.domain.entity.RolePrivilegeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface RolePrivilegeDao extends BaseMapper<RolePrivilegeEntity> {

    /**
     * 根据角色id删除
     *
     * @param roleId
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 删除权限所关联的角色信息
     *
     * @param privilegeKeyList
     */
    void deleteByPrivilegeKey(@Param("privilegeKeyList") List<String> privilegeKeyList);

    /**
     * 批量添加
     *
     * @param rolePrivilegeList
     */
    void batchInsert(List<RolePrivilegeEntity> rolePrivilegeList);

    /**
     * 查询某批角色的权限
     *
     * @param roleIds
     * @return
     */
    List<PrivilegeEntity> listByRoleIds(@Param("roleIds") List<Long> roleIds, @Param("normalStatus") Integer normalStatus);

    /**
     * 查询某个角色的权限
     *
     * @param roleId
     * @return
     */
    List<PrivilegeEntity> listByRoleId(@Param("roleId") Long roleId);
}

package com.anonym.module.privilege;

import com.anonym.module.privilege.domain.PrivilegeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface PrivilegeDao extends BaseMapper<PrivilegeEntity> {

    /**
     * 根据父节点id查询
     *
     * @param parentId
     * @return
     */
    List<PrivilegeEntity> selectByParentId(@Param("parentId") Integer parentId);

    /**
     * 删除一批权限
     *
     * @param privilegeIds
     */
    void delPrivilege(@Param("privilegeIds") List<Integer> privilegeIds);

    /**
     * 根据父节点以及权限类型查询
     *
     * @param parentId
     * @param type
     * @return
     */
    List<PrivilegeEntity> selectByParentIdAndType(@Param("parentId") Integer parentId, @Param("type") Integer type);

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    List<PrivilegeEntity> selectByExcludeType(@Param("type") Integer type, @Param("scope") Integer scope);

    /**
     * 根据类型查询
     *
     * @param type
     * @return
     */
    List<PrivilegeEntity> selectByType(@Param("type") Integer type);

    /**
     * 查询所有权限
     *
     * @return
     */
    List<PrivilegeEntity> selectAll();

    /**
     * 根据禁用状态查询
     *
     * @param isEnable
     * @return
     */
    List<PrivilegeEntity> listByEnable(@Param("isEnable") Integer isEnable, @Param("scope") Integer scope);

}

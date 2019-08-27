package com.anonym.module.role;

import com.anonym.module.role.domain.RoleEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    /**
     * 根据角色名字查询角色
     *
     * @param name
     * @return
     */
    RoleEntity getByRoleName(@Param("roleName") String name);

}

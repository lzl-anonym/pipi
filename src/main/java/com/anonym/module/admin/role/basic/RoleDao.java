package com.anonym.module.admin.role.basic;

import com.anonym.module.admin.role.basic.domain.entity.RoleEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {

    RoleEntity getByRoleName(@Param("roleName") String name);

}

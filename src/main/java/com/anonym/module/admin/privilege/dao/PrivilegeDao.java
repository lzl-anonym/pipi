package com.anonym.module.admin.privilege.dao;

import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface PrivilegeDao extends BaseMapper<PrivilegeEntity> {


    void delByKeyList(@Param("keyList") List<String> keyList);


    void delByParentKeyList(@Param("keyList") List<String> keyList);


    void batchInsert(List<PrivilegeEntity> privilegeList);


    void batchUpdate(@Param("updateList") List<PrivilegeEntity> privilegeList);


    List<PrivilegeEntity> selectByParentKey(@Param("parentKey") String parentKey);


    PrivilegeEntity selectByKey(@Param("key") String key);


    List<PrivilegeEntity> selectByExcludeType(@Param("type") Integer type);


    List<PrivilegeEntity> selectByType(@Param("type") Integer type);


    List<PrivilegeEntity> selectAll();

}

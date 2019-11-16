package com.anonym.module.fuseuser;

import com.anonym.module.fuseuser.domain.FaceFuseUserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author lizongliang
 * @date 2019-11-16 14:54
 */
@Component
@Mapper
public interface FaceFuseUserDao extends BaseMapper<FaceFuseUserEntity> {

    FaceFuseUserEntity selectByPhone(String phone);

}

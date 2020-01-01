package com.anonym.module.fuseuser;

import com.anonym.module.fuseuser.domain.FaceFuseUserEntity;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordQueryDTO;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordVO;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-11-16 14:54
 */
@Component
@Mapper
public interface FaceFuseUserDao extends BaseMapper<FaceFuseUserEntity> {

	FaceFuseUserEntity selectByPhone(String phone);

}

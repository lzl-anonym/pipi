package com.anonym.module.fuseuser.fuse.domian;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author lizongliang
 * @date 2019-11-16 17:45
 */
@Component
@Mapper
public interface FaceFuseDao extends BaseMapper<FaceFuseEntity> {
}

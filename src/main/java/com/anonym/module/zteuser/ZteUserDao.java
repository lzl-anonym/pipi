package com.anonym.module.zteuser;

import com.anonym.module.zteuser.domain.ZteUserQueryDTO;
import com.anonym.module.zteuser.domain.ZteUserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-12-27 10:47
 */
@Mapper
@Component
public interface ZteUserDao extends BaseMapper<ZteUserEntity> {

    /**
     * 分页查询
     * @param queryDTO
     * @return ZteUserEntity
    */
    List<ZteUserEntity> queryByPage(Pagination page, @Param("queryDTO") ZteUserQueryDTO queryDTO);

    /**
     * 根据id删除
     * @param id
     * @return
    */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除
     * @param idList
     * @return
    */
    void deleteByIds(@Param("idList") List<Long> idList);
}

package com.anonym.module.userloginlog;

import com.anonym.module.userloginlog.domain.UserLoginLogQueryDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * [ 用户登录日志 ]
 */
@Mapper
@Component
public interface UserLoginLogDao extends BaseMapper<UserLoginLogEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return UserLoginLogEntity
     */
    List<UserLoginLogEntity> queryByPage(Pagination page, @Param("queryDTO") UserLoginLogQueryDTO queryDTO);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    void deleteByIds(@Param("idList") List<Long> idList);
}

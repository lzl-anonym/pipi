package com.anonym.module.user.basic;

import com.anonym.module.user.basic.domain.UserAdminQueryDTO;
import com.anonym.module.user.basic.domain.UserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * t_user 数据表 dao
 */
@Component
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 新增 并获取主键id
     *
     * @param userEntity
     * @return
     */
    @Override
    Integer insert(UserEntity userEntity);

    /**
     * 更新最后一次登录ip 以及 登录时间
     *
     * @param userId
     * @param ip
     * @return
     */
    int updateLastLogin(@Param("userId") Long userId, @Param("ip") String ip);

    /**
     * 更新密码
     *
     * @param userId
     * @param pwd
     * @return
     */
    int updatePwd(@Param("userId") Long userId, @Param("pwd") String pwd);

    /**
     * 更新用户信息
     *
     * @param userEntity
     * @return
     */
    int updateUser(UserEntity userEntity);

    /**
     * 分页查询用户列表
     *
     * @param page
     * @param query
     * @return
     */
    List<UserEntity> query(Page page, @Param("query") UserAdminQueryDTO query);

    /**
     * 根据用户id 查询
     *
     * @param userId
     * @return
     */
    int countByUserId(Long userId);


    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    UserEntity selectByPhone(@Param("phone") String phone);

    /**
     * 查询用户
     *
     * @param excludeUserId
     * @param phone
     * @return
     */
    UserEntity selectUser(@Param("excludeUserId") Long excludeUserId, @Param("phone") String phone);


}

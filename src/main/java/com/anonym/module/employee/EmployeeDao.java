package com.anonym.module.employee;


import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.employee.domain.EmployeeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {

    /**
     * 登录
     *
     * @param loginName
     * @param loginPwd
     * @return
     */
    EmployeeDTO login(@Param("loginName") String loginName, @Param("loginPwd") String loginPwd);

}

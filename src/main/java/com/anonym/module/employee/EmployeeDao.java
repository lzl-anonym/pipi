package com.anonym.module.employee;


import com.anonym.module.employee.domain.EmployeeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {

}

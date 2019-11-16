package com.anonym.module.admin.employee.login.domain;

import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import lombok.Data;


@Data
public class LoginCacheDTO {

    /**
     * 基本信息
     */
    private EmployeeDTO employeeDTO;

    /**
     * 过期时间
     */
    private Long expireTime;

}

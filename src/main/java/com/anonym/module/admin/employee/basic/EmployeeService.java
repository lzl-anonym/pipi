package com.anonym.module.admin.employee.basic;

import com.anonym.module.admin.department.DepartmentDao;
import com.anonym.module.admin.employee.login.EmployeeLoginCacheService;
import com.anonym.module.admin.role.roleemployee.RoleEmployeeDao;
import com.anonym.module.systemconfig.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 员工管理
 */
@Service
public class EmployeeService {

    private static final String RESET_PASSWORD = "123456";

    @Value("${jwt.key}")
    private String jwtKey;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private EmployeeLoginCacheService loginCacheService;


    @Autowired
    private SystemConfigService systemConfigService;


}

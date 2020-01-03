package com.anonym.module.admin.role.roleemployee;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.admin.department.DepartmentDao;
import com.anonym.module.admin.department.domain.entity.DepartmentEntity;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.admin.role.basic.RoleDao;
import com.anonym.module.admin.role.basic.RoleResponseCodeConst;
import com.anonym.module.admin.role.basic.domain.dto.RoleBatchDTO;
import com.anonym.module.admin.role.basic.domain.dto.RoleQueryDTO;
import com.anonym.module.admin.role.basic.domain.dto.RoleSelectedVO;
import com.anonym.module.admin.role.basic.domain.dto.RoleVO;
import com.anonym.module.admin.role.roleemployee.domain.RoleEmployeeEntity;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色管理业务
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DepartmentDao departmentDao;






}

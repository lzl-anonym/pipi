package com.anonym.module.admin.employee.basic;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.admin.department.DepartmentDao;
import com.anonym.module.admin.department.domain.entity.DepartmentEntity;
import com.anonym.module.admin.employee.EmployeeResponseCodeConst;
import com.anonym.module.admin.employee.basic.domain.dto.*;
import com.anonym.module.admin.employee.basic.domain.entity.EmployeeEntity;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.admin.employee.login.EmployeeLoginCacheService;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.admin.position.PositionService;
import com.anonym.module.admin.position.domain.dto.PositionRelationAddDTO;
import com.anonym.module.admin.position.domain.dto.PositionRelationResultDTO;
import com.anonym.module.admin.role.roleemployee.RoleEmployeeDao;
import com.anonym.module.admin.role.roleemployee.domain.RoleEmployeeEntity;
import com.anonym.module.systemconfig.SystemConfigService;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.constant.SystemConfigResponseCodeConst;
import com.anonym.module.systemconfig.domain.dto.SystemConfigDTO;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartDigestUtil;
import com.anonym.utils.SmartPageUtil;
import com.anonym.utils.SmartVerificationUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private PositionService positionService;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 查询员工列表
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<EmployeeVO>> selectEmployeeList(EmployeeQueryDTO queryDTO) {
        Page pageParam = SmartPageUtil.convert2PageQuery(queryDTO);
        queryDTO.setIsDelete(JudgeEnum.NO.getValue());
        List<EmployeeDTO> empList = employeeDao.selectEmployeeList(pageParam, queryDTO);

        //超管查询
        SystemConfigDTO systemConfigDTO = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        if (systemConfigDTO == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        empList.stream().forEach(e -> {
            List<PositionRelationResultDTO> positionRelationList = positionService.queryPositionByEmployeeId(e.getId());
            if (CollectionUtils.isNotEmpty(positionRelationList)) {
                e.setPositionRelationList(positionRelationList);
                e.setPositionName(positionRelationList.stream().map(PositionRelationResultDTO::getPositionName).collect(Collectors.joining(",")));
            }
            Long supermanId = Long.parseLong(systemConfigDTO.getConfigValue());
            e.setIsSuper(supermanId.equals(e.getId()));
        });
        return ResponseDTO.succData(SmartPageUtil.convert2PageResult(pageParam, empList, EmployeeVO.class));
    }

    /**
     * 新增员工
     *
     * @param employeeAddDto
     * @param requestToken
     * @return
     */
    public ResponseDTO<String> addEmployee(EmployeeAddDTO employeeAddDto, LoginTokenDTO requestToken) {
        EmployeeEntity entity = SmartBeanUtil.copy(employeeAddDto, EmployeeEntity.class);
        if (StringUtils.isNotEmpty(employeeAddDto.getIdCard())) {
            boolean checkResult = Pattern.matches(SmartVerificationUtil.IDENTITY_CARD, employeeAddDto.getIdCard());
            if (!checkResult) {
                return ResponseDTO.wrap(EmployeeResponseCodeConst.ID_CARD_ERROR);
            }
        }
        if (StringUtils.isNotEmpty(employeeAddDto.getBirthday())) {
            boolean checkResult = Pattern.matches(SmartVerificationUtil.IS_DATE, employeeAddDto.getBirthday());
            if (!checkResult) {
                return ResponseDTO.wrap(EmployeeResponseCodeConst.BIRTHDAY_ERROR);
            }
        }
        //同名员工
        EmployeeDTO sameNameEmployee = employeeDao.getByLoginName(entity.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
        if (null != sameNameEmployee) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_NAME_EXISTS);
        }
        //同电话员工
        EmployeeDTO samePhoneEmployee = employeeDao.getByPhone(entity.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
        if (null != samePhoneEmployee) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.PHONE_EXISTS);
        }
//        Long departmentId = entity.getDepartmentId();
//        DepartmentEntity department = departmentDao.selectById(departmentId);
//        if (department == null) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.DEPT_NOT_EXIST);
//        }

        //如果没有密码  默认设置为123456
        String pwd = entity.getLoginPwd();
        if (StringUtils.isBlank(pwd)) {
            entity.setLoginPwd(SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, RESET_PASSWORD));
        } else {
            entity.setLoginPwd(SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, entity.getLoginPwd()));
        }

        entity.setCreateUser(requestToken.getId());
        if (StringUtils.isEmpty(entity.getBirthday())) {
            entity.setBirthday(null);
        }
        employeeDao.insert(entity);

        if (CollectionUtils.isNotEmpty(employeeAddDto.getPositionIdList())) {
            PositionRelationAddDTO positionRelAddDTO = new PositionRelationAddDTO(employeeAddDto.getPositionIdList(), entity.getId());
            //存储所选岗位信息
            positionService.addPositionRelation(positionRelAddDTO);
        }
        return ResponseDTO.succ();
    }

    /**
     * 更新禁用状态
     *
     * @param employeeId
     * @param status
     * @return
     */
    public ResponseDTO<String> updateStatus(Long employeeId, Integer status) {
        if (null == employeeId) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.EMP_NOT_EXISTS);
        }
        EmployeeEntity entity = employeeDao.selectById(employeeId);
        if (null == entity) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.EMP_NOT_EXISTS);
        }
        //--产品要求此处不需要判断，前端绑定商家，提示该手机号对应账号，已被禁用，无法绑定即可
        //1.禁用，需判断是否关联店铺，如果关联店铺，需判断店铺是否处于禁用状态，如果非禁用状态,则不能禁用该账户
//        ShopEntity shopEntity = shopDao.selectShopByManagerId(employeeId);
//        if (shopEntity != null && shopEntity.getEnableFlag()) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.NOT_ALLOW_ENABLE_STOP);
//        }

        List<Long> empIds = Lists.newArrayList();
        empIds.add(employeeId);
        employeeDao.batchUpdateStatus(empIds, status);
        loginCacheService.updateCacheEmployee(employeeId);
        return ResponseDTO.succ();
    }

    /**
     * 批量更新员工状态
     *
     * @param batchUpdateStatusDTO
     * @return
     */
    public ResponseDTO<String> batchUpdateStatus(EmployeeBatchUpdateStatusDTO batchUpdateStatusDTO) {
        employeeDao.batchUpdateStatus(batchUpdateStatusDTO.getEmployeeIds(), batchUpdateStatusDTO.getStatus());
        loginCacheService.batchUpdateCacheEmployee(batchUpdateStatusDTO.getEmployeeIds());
        return ResponseDTO.succ();
    }

    /**
     * 更新员工
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateEmployee(EmployeeUpdateDTO updateDTO) {
        Long employeeId = updateDTO.getId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.EMP_NOT_EXISTS);
        }
        if (StringUtils.isNotBlank(updateDTO.getIdCard())) {
            boolean checkResult = Pattern.matches(SmartVerificationUtil.IDENTITY_CARD, updateDTO.getIdCard());
            if (!checkResult) {
                return ResponseDTO.wrap(EmployeeResponseCodeConst.ID_CARD_ERROR);
            }
        }
        if (StringUtils.isNotEmpty(updateDTO.getBirthday())) {
            boolean checkResult = Pattern.matches(SmartVerificationUtil.IS_DATE, updateDTO.getBirthday());
            if (!checkResult) {
                return ResponseDTO.wrap(EmployeeResponseCodeConst.BIRTHDAY_ERROR);
            }
        }
        Long departmentId = updateDTO.getDepartmentId();
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.DEPT_NOT_EXIST);
        }
        EmployeeDTO sameNameEmployee = employeeDao.getByLoginName(updateDTO.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
        if (null != sameNameEmployee && !sameNameEmployee.getId().equals(employeeId)) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_NAME_EXISTS);
        }
        EmployeeDTO samePhoneEmployee = employeeDao.getByPhone(updateDTO.getLoginName(), EmployeeStatusEnum.NORMAL.getValue());
        if (null != samePhoneEmployee && !samePhoneEmployee.getId().equals(employeeId)) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.PHONE_EXISTS);
        }
        String newPwd = updateDTO.getLoginPwd();
        if (!StringUtils.isBlank(newPwd)) {
            updateDTO.setLoginPwd(SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, updateDTO.getLoginPwd()));
        } else {
            updateDTO.setLoginPwd(employeeEntity.getLoginPwd());
        }
        EmployeeEntity entity = SmartBeanUtil.copy(updateDTO, EmployeeEntity.class);
        entity.setUpdateTime(new Date());
        if (StringUtils.isEmpty(entity.getBirthday())) {
            entity.setBirthday(null);
        }
        if (CollectionUtils.isNotEmpty(updateDTO.getPositionIdList())) {
            //删除旧的关联关系 添加新的关联关系
            positionService.removePositionRelation(entity.getId());
            PositionRelationAddDTO positionRelAddDTO = new PositionRelationAddDTO(updateDTO.getPositionIdList(), entity.getId());
            positionService.addPositionRelation(positionRelAddDTO);
        }
        entity.setIsDisabled(employeeEntity.getIsDisabled());
        entity.setIsLeave(employeeEntity.getIsLeave());
        entity.setCreateUser(employeeEntity.getCreateUser());
        entity.setCreateTime(employeeEntity.getCreateTime());
        entity.setUpdateTime(new Date());
        employeeDao.updateById(entity);
        loginCacheService.updateCacheEmployee(employeeId);
        return ResponseDTO.succ();
    }

    /**
     * 删除员工
     *
     * @param employeeId 员工ID
     * @return
     */
    public ResponseDTO<String> deleteEmployeeById(Long employeeId) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.EMP_NOT_EXISTS);
        }

        //校验禁用之后可以删除
        if (JudgeEnum.NO.equalsValue(employeeEntity.getIsDisabled())) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.NOT_ALLOW_DELETE);
        }

        //假删
        employeeEntity.setIsDelete(JudgeEnum.YES.getValue().longValue());
        employeeDao.updateById(employeeEntity);
        loginCacheService.removeLoginCache(employeeId);
        return ResponseDTO.succ();
    }

    /**
     * 更新用户角色
     *
     * @param updateRolesDTO
     * @return
     */
    public ResponseDTO<String> updateRoles(EmployeeUpdateRolesDTO updateRolesDTO) {
        roleEmployeeDao.deleteByEmployeeId(updateRolesDTO.getEmployeeId());
        if (CollectionUtils.isNotEmpty(updateRolesDTO.getRoleIds())) {
            List<RoleEmployeeEntity> roleEmployeeEntities = Lists.newArrayList();
            RoleEmployeeEntity roleEmployeeEntity;
            for (Long roleId : updateRolesDTO.getRoleIds()) {
                roleEmployeeEntity = new RoleEmployeeEntity();
                roleEmployeeEntity.setEmployeeId(updateRolesDTO.getEmployeeId());
                roleEmployeeEntity.setRoleId(roleId);
                roleEmployeeEntities.add(roleEmployeeEntity);
            }
            roleEmployeeDao.batchInsert(roleEmployeeEntities);
        }
        return ResponseDTO.succ();
    }

    /**
     * 更新密码
     *
     * @param updatePwdDTO
     * @param requestToken
     * @return
     */
    public ResponseDTO<String> updatePwd(EmployeeUpdatePwdDTO updatePwdDTO, LoginTokenDTO requestToken) {
        Long employeeId = requestToken.getId();
        EmployeeEntity employee = employeeDao.selectById(employeeId);
        if (employee == null) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.EMP_NOT_EXISTS);
        }
        if (!employee.getLoginPwd().equals(SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, updatePwdDTO.getOldPwd()))) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.PASSWORD_ERROR);
        }
        employee.setLoginPwd(SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, updatePwdDTO.getPwd()));
        employeeDao.updateById(employee);
        return ResponseDTO.succ();
    }

    public ResponseDTO<List<EmployeeVO>> getEmployeeByDeptId(Long departmentId) {
        List<EmployeeVO> list = employeeDao.getEmployeeIdByDeptId(departmentId);
        return ResponseDTO.succData(list);
    }

    /**
     * 重置密码
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO resetPasswd(Integer employeeId) {
        String md5Password = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, RESET_PASSWORD);
        employeeDao.updatePassword(employeeId, md5Password);
        return ResponseDTO.succ();
    }

    /**
     * 判断是否为超级管理员
     *
     * @param userId
     * @return
     */
    public Boolean isSuper(Long userId) {
        if (userId == null) {
            return false;
        }
        //查询超级管理员ID
        SystemConfigDTO systemConfigDTO = systemConfigService.getCacheByKey(SystemConfigEnum.Key.EMPLOYEE_SUPERMAN);
        if (systemConfigDTO == null) {
            return false;
        }
        String configValue = systemConfigDTO.getConfigValue();
        Long superManId = Long.parseLong(configValue);
        return superManId.equals(userId);
    }

}

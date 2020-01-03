package com.anonym.module.admin.employee.login;

import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.admin.department.DepartmentDao;
import com.anonym.module.admin.employee.EmployeeResponseCodeConst;
import com.anonym.module.admin.employee.basic.EmployeeDao;
import com.anonym.module.admin.employee.basic.EmployeeStatusEnum;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeLoginFormDTO;
import com.anonym.module.admin.employee.basic.domain.dto.KaptchaVO;
import com.anonym.module.admin.employee.login.domain.EmployeeLoginDetailVO;
import com.anonym.module.admin.employee.login.domain.LoginPrivilegeDTO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.admin.privilege.domain.entity.PrivilegeEntity;
import com.anonym.module.admin.privilege.service.PrivilegeEmployeeService;
import com.anonym.module.log.LogService;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartDigestUtil;
import com.anonym.utils.SmartRandomUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
public class EmployeeLoginService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmployeeLoginCacheService loginCacheService;


    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Autowired
    private EmployeeLoginTokenService loginTokenService;




    /**
     * 登陆
     *
     * @param loginForm 登录名 密码
     * @return 登录用户基本信息
     */
    public ResponseDTO<EmployeeLoginDetailVO> login(@Valid EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
        String loginPwd = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, loginForm.getLoginPwd());
        EmployeeDTO employeeDTO = employeeDao.login(loginForm.getLoginName(), loginPwd);
        if (null == employeeDTO) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_FAILED);
        }
        if (EmployeeStatusEnum.DISABLED.equalsValue(employeeDTO.getIsDisabled())) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.IS_DISABLED);
        }
        String compactJws = loginTokenService.generateToken(employeeDTO);


        EmployeeLoginDetailVO loginDTO = SmartBeanUtil.copy(employeeDTO, EmployeeLoginDetailVO.class);

        loginDTO.setPrivilegeList(initEmployeePrivilege(employeeDTO.getId()));

        loginDTO.setXAccessToken(compactJws);

        Boolean isSuperman = privilegeEmployeeService.isSuperman(loginDTO.getId());
        loginDTO.setIsSuperMan(isSuperman);


        loginCacheService.addLoginCache(employeeDTO);

        return ResponseDTO.succData(loginDTO);
    }

    public ResponseDTO<Boolean> logoutByToken(LoginTokenDTO requestToken) {
        privilegeEmployeeService.removeCache(requestToken.getId());
        loginCacheService.removeLoginCache(requestToken.getId());
        return ResponseDTO.succ();
    }



    /**
     * 初始化员工权限
     *
     * @param employeeId
     * @return
     */
    public List<LoginPrivilegeDTO> initEmployeePrivilege(Long employeeId) {
        List<LoginPrivilegeDTO> loginPrivilegeList = Lists.newArrayList();
        List<PrivilegeEntity> privilegeList = privilegeEmployeeService.getPrivilegesByEmployeeId(employeeId);
        privilegeEmployeeService.updateCachePrivilege(employeeId, privilegeList);
        loginPrivilegeList = this.buildPrivilegeTree(privilegeList);
        return loginPrivilegeList;
    }

    private List<LoginPrivilegeDTO> buildPrivilegeTree(List<PrivilegeEntity> privilegeEntityList) {
        List<LoginPrivilegeDTO> privilegeTree = Lists.newArrayList();
        List<PrivilegeEntity> rootPrivilege = privilegeEntityList.stream().filter(e -> e.getParentKey() == null).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootPrivilege)) {
            return privilegeTree;
        }
        privilegeTree = SmartBeanUtil.copyList(rootPrivilege, LoginPrivilegeDTO.class);
        privilegeTree.forEach(e -> e.setChildren(Lists.newArrayList()));
        this.buildChildPrivilegeList(privilegeEntityList, privilegeTree);
        return privilegeTree;
    }

    private void buildChildPrivilegeList(List<PrivilegeEntity> privilegeEntityList, List<LoginPrivilegeDTO> parentMenuList) {
        List<String> parentKeyList = parentMenuList.stream().map(LoginPrivilegeDTO::getKey).collect(Collectors.toList());
        List<PrivilegeEntity> childEntityList = privilegeEntityList.stream().filter(e -> parentKeyList.contains(e.getParentKey())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childEntityList)) {
            return;
        }
        Map<String, List<PrivilegeEntity>> listMap = childEntityList.stream().collect(Collectors.groupingBy(PrivilegeEntity::getParentKey));
        for (LoginPrivilegeDTO loginPrivilegeDTO : parentMenuList) {
            String key = loginPrivilegeDTO.getKey();
            List<PrivilegeEntity> privilegeEntities = listMap.get(key);
            if (CollectionUtils.isEmpty(privilegeEntities)) {
                continue;
            }
            List<LoginPrivilegeDTO> privilegeList = SmartBeanUtil.copyList(privilegeEntities, LoginPrivilegeDTO.class);
            privilegeList.forEach(e -> e.setChildren(Lists.newArrayList()));
            loginPrivilegeDTO.setChildren(privilegeList);
            this.buildChildPrivilegeList(privilegeEntityList, privilegeList);
        }
    }
}

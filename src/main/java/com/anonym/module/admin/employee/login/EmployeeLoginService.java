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
    private DepartmentDao departmentDao;

    @Autowired
    private PrivilegeEmployeeService privilegeEmployeeService;

    @Autowired
    private EmployeeLoginTokenService loginTokenService;

    @Autowired
    private LogService logService;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private ValueOperations<String, String> redisValueOperations;


    /**
     * 登陆
     *
     * @param loginForm 登录名 密码
     * @return 登录用户基本信息
     */
    public ResponseDTO<EmployeeLoginDetailVO> login(@Valid EmployeeLoginFormDTO loginForm, HttpServletRequest request) {
//        String redisVerificationCode = redisValueOperations.get(loginForm.getCodeUuid());
//        //增加删除已使用的验证码方式 频繁登录      暂时移除验证码校验
//        redisValueOperations.getOperations().delete(loginForm.getCodeUuid());
//        if (StringUtils.isEmpty(redisVerificationCode)) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID);
//        }
//        if (!redisVerificationCode.equalsIgnoreCase(loginForm.getCode())) {
//            return ResponseDTO.wrap(EmployeeResponseCodeConst.VERIFICATION_CODE_INVALID);
//        }
        String loginPwd = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, loginForm.getLoginPwd());
        EmployeeDTO employeeDTO = employeeDao.login(loginForm.getLoginName(), loginPwd);
        if (null == employeeDTO) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_FAILED);
        }
        if (EmployeeStatusEnum.DISABLED.equalsValue(employeeDTO.getIsDisabled())) {
            return ResponseDTO.wrap(EmployeeResponseCodeConst.IS_DISABLED);
        }
        //jwt token赋值
        String compactJws = loginTokenService.generateToken(employeeDTO);


        EmployeeLoginDetailVO loginDTO = SmartBeanUtil.copy(employeeDTO, EmployeeLoginDetailVO.class);

        //获取前端功能权限
        loginDTO.setPrivilegeList(initEmployeePrivilege(employeeDTO.getId()));

        loginDTO.setXAccessToken(compactJws);
//        DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());
//        loginDTO.setDepartmentName(departmentEntity.getName());

        //判断是否为超管
        Boolean isSuperman = privilegeEmployeeService.isSuperman(loginDTO.getId());
        loginDTO.setIsSuperMan(isSuperman);


        //登陆操作日志
//        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
//        EmployeeLoginLogEntity logEntity =
//            EmployeeLoginLogEntity.builder().userId(employeeDTO.getId()).userName(employeeDTO.getActualName()).remoteIp(SmartIPUtil.getRemoteIp(request)).remotePort(request.getRemotePort()).remoteAddress(SmartIPUtil.getRemoteLocation(request)).remoteBrowser(userAgent.getBrowser().getName()).remoteOs(userAgent.getOperatingSystem().getName()).loginStatus(JudgeEnum.YES.getValue()).build();
//        logService.addLog(logEntity);

        loginCacheService.addLoginCache(employeeDTO);

        return ResponseDTO.succData(loginDTO);
    }

    /**
     * 手机端退出登陆，清除token缓存
     *
     * @param requestToken
     * @return 退出登陆是否成功，bool
     */
    public ResponseDTO<Boolean> logoutByToken(LoginTokenDTO requestToken) {
        privilegeEmployeeService.removeCache(requestToken.getId());
        loginCacheService.removeLoginCache(requestToken.getId());
        return ResponseDTO.succ();
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public ResponseDTO<KaptchaVO> verificationCode() {
        KaptchaVO kaptchaDTO = new KaptchaVO();
        String uuid = UUID.randomUUID().toString();
        String kaptchaText = SmartRandomUtil.generateRandomNum(4);

        String base64Code = "";

        BufferedImage image = defaultKaptcha.createImage(kaptchaText);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("verificationCode exception .{}", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    log.error("verificationCode outputStream close exception .{}", e);
                }
            }
        }
        kaptchaDTO.setUuid(uuid);
        kaptchaDTO.setCode("data:image/png;base64," + base64Code);
        redisValueOperations.set(uuid, kaptchaText, 60L, TimeUnit.SECONDS);
        return ResponseDTO.succData(kaptchaDTO);
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

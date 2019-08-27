package com.anonym.module.employee;

import com.anonym.common.constant.CommonConst;
import com.anonym.common.constant.SuccessStatusEnum;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.token.RequestTokenDTO;
import com.anonym.common.token.RequestTokenService;
import com.anonym.module.department.DepartmentDao;
import com.anonym.module.department.domain.DepartmentEntity;
import com.anonym.module.employee.domain.EmployeeDTO;
import com.anonym.module.employee.domain.EmployeeLoginDTO;
import com.anonym.module.employee.domain.EmployeeLoginDetailDTO;
import com.anonym.module.privilege.EmployeePrivilegeCacheService;
import com.anonym.module.privilege.constant.PrivilegeTypeEnum;
import com.anonym.module.privilege.domain.PrivilegeEntity;
import com.anonym.module.userloginlog.LogService;
import com.anonym.module.userloginlog.domain.UserLoginLogEntity;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartDigestUtil;
import com.anonym.utils.SmartIPUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class LoginService {

	@Autowired
	private RequestTokenService requestTokenService;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private EmployeePrivilegeCacheService employeePrivilegeCacheService;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private LogService logService;


	public ResponseDTO<EmployeeLoginDetailDTO> login(@Valid EmployeeLoginDTO employeeLoginDTOloginDTO, HttpServletRequest request) {

		String loginPwd = SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, employeeLoginDTOloginDTO.getLoginPwd());
		EmployeeDTO employeeDTO = employeeDao.login(employeeLoginDTOloginDTO.getLoginName(), loginPwd);
		if (null == employeeDTO) {
			return ResponseDTO.wrap(EmployeeResponseCodeConst.LOGIN_FAILED);
		}
		if (EmployeeStatusEnum.DISABLED.equalsValue(employeeDTO.getIsDisabled())) {
			return ResponseDTO.wrap(EmployeeResponseCodeConst.IS_DISABLED);
		}
		//jwt token赋值
		RequestTokenDTO<EmployeeDTO> requestTokenDTO = new RequestTokenDTO();
		requestTokenDTO.setId(employeeDTO.getId());
		requestTokenDTO.setUserType(CommonConst.UserType.BACK);
		requestTokenDTO.setData(employeeDTO);


		String compactJws = requestTokenService.generateToken(requestTokenDTO);

		EmployeeLoginDetailDTO loginDTO = SmartBeanUtil.copy(employeeDTO, EmployeeLoginDetailDTO.class);

		//获取前端功能权限
		loginDTO.setPrivilegeList(initEmployeePrivilege(employeeDTO.getId()));

		loginDTO.setXAccessToken(compactJws);

		DepartmentEntity departmentEntity = departmentDao.selectById(employeeDTO.getDepartmentId());

		loginDTO.setDepartmentName(departmentEntity.getName());

		//判断是否为超管
		Integer supermanId = employeePrivilegeCacheService.getSupermanId();
		if (supermanId.equals(loginDTO.getId())) {
			loginDTO.setIsSuperMan(Boolean.TRUE);
		} else {
			loginDTO.setIsSuperMan(Boolean.FALSE);
		}

		//登陆操作日志
		// TODO: 2019-08-28    暂时不添加登陆日志
/*		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		UserLoginLogEntity logEntity =
				UserLoginLogEntity.builder().userId(employeeDTO.getId()).userName(employeeDTO.getActualName()).userType(CommonConst.UserType.BACK).remoteIp(SmartIPUtil.getRemoteIp(request)).remotePort(request.getRemotePort()).remoteAddress(SmartIPUtil.getRemoteLocation(request)).remoteBrowser(userAgent.getBrowser().getName()).remoteOs(userAgent.getOperatingSystem().getName()).loginStatus(SuccessStatusEnum.SUCCESS.getStatus()).build();
		logService.addLog(logEntity);*/


		return ResponseDTO.succData(loginDTO);
	}


	/**
	 * 初始化员工权限
	 *
	 * @param employeeId
	 * @return
	 */
	public Map<String, Map<String, Map<String, List<String>>>> initEmployeePrivilege(Integer employeeId) {
		Map<String, Map<String, Map<String, List<String>>>> privilegeMap = new HashMap<>(16);
		List<PrivilegeEntity> privilegeList = employeePrivilegeCacheService.getPrivilegesByEmployeeId(employeeId);
		employeePrivilegeCacheService.updateCachePrivilege(employeeId, privilegeList);

		List<PrivilegeEntity> moduleList = privilegeList.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.MODULE.getValue())).collect(Collectors.toList());
		List<PrivilegeEntity> childrenModuleList = privilegeList.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.CHILDREN_MODULE.getValue())).collect(Collectors.toList());
		List<PrivilegeEntity> pageList = privilegeList.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.PAGE.getValue())).collect(Collectors.toList());
		List<PrivilegeEntity> pointList = privilegeList.stream().filter(e -> e.getType().equals(PrivilegeTypeEnum.POINTS.getValue())).collect(Collectors.toList());

		//模块层
		moduleList.forEach(item -> {
			Integer moduleId = item.getId();

			//二级map，存放 子模块/页面
			Map<String, Map<String, List<String>>> secondMap = new HashMap<>(20);

			//子模块层
			List<PrivilegeEntity> childModuleList = childrenModuleList.stream().filter(e -> e.getParentId().equals(moduleId)).collect(Collectors.toList());
			childModuleList.forEach(childModule -> {
				Integer childModuleId = childModule.getId();
				//页面层
				List<PrivilegeEntity> childPageList = pageList.stream().filter(e -> e.getParentId().equals(childModuleId)).collect(Collectors.toList());
				Map<String, List<String>> pageMap = new HashMap<>(childPageList.size());
				childPageList.stream().forEach(page -> {
					Integer pageId = page.getId();
					//功能点层
					List<PrivilegeEntity> childPointList = pointList.stream().filter(e -> e.getParentId().equals(pageId)).collect(Collectors.toList());
					List<String> pointKeyList = childPointList.stream().map(PrivilegeEntity::getRouterKey).collect(Collectors.toList());
					pageMap.put(page.getRouterKey(), pointKeyList);
				});
				secondMap.put(childModule.getRouterKey(), pageMap);
			});

			//页面层
			List<PrivilegeEntity> childPageList = pageList.stream().filter(e -> e.getParentId().equals(moduleId)).collect(Collectors.toList());
			childPageList.stream().forEach(page -> {
				Map<String, List<String>> pageMap = new HashMap<>(childPageList.size());
				//功能点层
				List<PrivilegeEntity> childPointList = pointList.stream().filter(e -> e.getParentId().equals(page.getId())).collect(Collectors.toList());
				pageMap.put(page.getRouterKey(), childPointList.stream().map(PrivilegeEntity::getRouterKey).collect(Collectors.toList()));
				secondMap.put(page.getRouterKey(), pageMap);
			});

			privilegeMap.put(item.getRouterKey(), secondMap);
		});

		return privilegeMap;
	}
}

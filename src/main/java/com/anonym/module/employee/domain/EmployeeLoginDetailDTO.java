package com.anonym.module.employee.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;


/**
 * 登录返回DTO
 *
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class EmployeeLoginDetailDTO extends EmployeeDTO {

	@ApiModelProperty("登陆token")
	private String xAccessToken;

	@ApiModelProperty("是否为超管")
	private Boolean isSuperMan;

	/**
	 * 1级Map key:员工ID
	 * 2级Map key:顶级模块路由名称
	 * 2级Map value:Map
	 * 3级Map key:页面or子模块路由名称_type
	 * 3级Map value:MAP
	 * <p>
	 * 4级Map：如果3级是页面则 key = 3级key页面  value均是功能点
	 * 如果3级是子模块 key = 页面，value=List《功能点》
	 */
	@ApiModelProperty("权限列表，type：0，子模块 1，页面")
	private Map<String, Map<String, Map<String, List<String>>>> privilegeList;

}

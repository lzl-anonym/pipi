package com.anonym.module.employee.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工实体类
 */
@Data
@TableName("t_employee")
public class EmployeeEntity implements Serializable {

	private static final long serialVersionUID = -8794328598524272806L;


	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 登录账号
	 */
	private String loginName;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 员工名称
	 */
	private String actualName;

	/**
	 * 别名
	 */
	private String nickName;

	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 身份证
	 */
	private String idCard;

	/**
	 * 出生日期
	 */
	private String birthday;


	/**
	 * 部门id
	 */
	private Integer departmentId;

	/**
	 * 是否离职
	 */
	private Integer isLeave;

	/**
	 * 是否被禁用
	 */
	private Integer isDisabled;
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 钉钉id
	 */
	private String dingId;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 微信openId
	 */
	private String wxOpenId;

	/**
	 * 创建者id
	 */
	private Integer createUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

}

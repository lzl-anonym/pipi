package com.anonym.module.employee;

import com.anonym.common.constant.ResponseCodeConst;

/**
 * 员工常量类
 * 3001-3999
 *
 * @author lidoudou
 * @date 2017年12月19日下午19:04:52
 */
public class EmployeeResponseCodeConst extends ResponseCodeConst {

    /**
     * 员工不存在
     */
    public static final EmployeeResponseCodeConst EMP_NOT_EXISTS = new EmployeeResponseCodeConst(1200, "员工不存在！");

    /**
     * 更新员工信息失败
     */
    public static final EmployeeResponseCodeConst UPDATE_FAILED = new EmployeeResponseCodeConst(1201, "员工更新失败！");

    /**
     * 部门信息不存在
     */
    public static final EmployeeResponseCodeConst DEPT_NOT_EXIST = new EmployeeResponseCodeConst(1202, "部门信息不存在!");

    /**
     * 用户名或密码错误
     */
    public static final EmployeeResponseCodeConst LOGIN_FAILED = new EmployeeResponseCodeConst(1203, "用户名或密码错误!");

    /**
     * 您的账号已被禁用，不得登录系统
     */
    public static final EmployeeResponseCodeConst IS_DISABLED = new EmployeeResponseCodeConst(1204, "您的账号已被禁用，不得登录系统!");

    /**
     * 登录名已存在
     */
    public static final EmployeeResponseCodeConst LOGIN_NAME_EXISTS = new EmployeeResponseCodeConst(1205, "登录名已存在!");

    /**
     * 密码输入有误，请重新输入 10115
     */
    public static final EmployeeResponseCodeConst PASSWORD_ERROR = new EmployeeResponseCodeConst(1206, "密码输入有误，请重新输入");

    /**
     * 手机号已存在
     */
    public static final EmployeeResponseCodeConst PHONE_EXISTS = new EmployeeResponseCodeConst(1207, "手机号已经存在");

    public static final EmployeeResponseCodeConst ID_CARD_ERROR = new EmployeeResponseCodeConst(1208, "请输入正确的身份证号");

    public static final EmployeeResponseCodeConst BIRTHDAY_ERROR = new EmployeeResponseCodeConst(1209, "生日格式不正确");

    public static final EmployeeResponseCodeConst VERIFICATION_CODE_INVALID = new EmployeeResponseCodeConst(1210, "验证码无效");

    public static final EmployeeResponseCodeConst NOT_CUSTOMER_MANAGER = new EmployeeResponseCodeConst(1211, "员工里没有客户经理，请调整人员岗位！");

    public static final EmployeeResponseCodeConst NOT_COMPANY = new EmployeeResponseCodeConst(1212, "请给商服配置负责的公司！");

    public EmployeeResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}

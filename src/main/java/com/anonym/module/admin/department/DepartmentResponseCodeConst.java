package com.anonym.module.admin.department;

import com.anonym.common.constant.ResponseCodeConst;


public class DepartmentResponseCodeConst extends ResponseCodeConst {

    /**
     * 部门不存在 1001
     */
    public static final DepartmentResponseCodeConst DEPT_NOT_EXISTS = new DepartmentResponseCodeConst(2001, "部门不存在！");


    public DepartmentResponseCodeConst(int code, String msg) {
        super(code, msg);
    }
}

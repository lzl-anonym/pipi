package com.anonym.module.log.orderoperatelog.constant;


public enum OrderOperateLogDefaultEmpEnum {

    DEFAULT_EMP(0, "系统");

    private Integer empId;

    private String empName;

    OrderOperateLogDefaultEmpEnum(Integer empId, String empName) {
        this.empId = empId;
        this.empName = empName;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

}


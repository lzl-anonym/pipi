package com.anonym.module.admin.datascope.constant;


public enum DataScopeWhereInTypeEnum {

    EMPLOYEE(0, "以员工IN"),

    DEPARTMENT(1, "以部门IN");

    private Integer type;

    private String desc;

    DataScopeWhereInTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

}

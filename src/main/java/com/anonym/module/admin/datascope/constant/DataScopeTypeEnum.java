package com.anonym.module.admin.datascope.constant;

public enum DataScopeTypeEnum {

    DEFAULT(0, 0, "默认类型", "数据范围样例");

    private Integer type;

    private Integer sort;

    private String name;

    private String desc;

    DataScopeTypeEnum(Integer type, Integer sort, String name, String desc) {
        this.type = type;
        this.sort = sort;
        this.name = name;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public Integer getSort() {
        return sort;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

}

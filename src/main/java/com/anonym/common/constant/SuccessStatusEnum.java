package com.anonym.common.constant;


public enum SuccessStatusEnum {

    FAIL(0, "失败"), SUCCESS(1, "成功");

    private Integer status;

    private String name;

    SuccessStatusEnum(Integer status, String empName) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}


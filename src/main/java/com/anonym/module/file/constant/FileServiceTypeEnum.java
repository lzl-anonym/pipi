package com.anonym.module.file.constant;

import com.anonym.common.domain.BaseEnum;

/**
 * 文件服务枚举类
 */
public enum FileServiceTypeEnum implements BaseEnum {

    /**
     * 本地文件服务
     */
    LOCAL(1, FileServiceNameConst.LOCAL, "阿里OSS文件服务"),

    /**
     * 阿里OSS文件服务
     */
    ALI_OSS(2, FileServiceNameConst.ALI_OSS, "阿里OSS文件服务"),

    /**
     * 七牛文件服务
     */
    QI_NIU_OSS(3, FileServiceNameConst.QI_NIU_OSS, "七牛文件服务"),

    /**
     * 华为文件服务
     */
    HW_OSS(3, FileServiceNameConst.HW_OSS, "华为文件服务");

    private Integer serviceType;

    private String serviceName;

    private String desc;

    FileServiceTypeEnum(Integer serviceType, String serviceName, String desc) {
        this.serviceType = serviceType;
        this.serviceName = serviceName;
        this.desc = desc;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Integer getValue() {
        return this.serviceType;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}

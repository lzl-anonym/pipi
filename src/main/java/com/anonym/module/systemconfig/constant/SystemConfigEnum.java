package com.anonym.module.systemconfig.constant;

/**
 * [ 系统配置常量类 ]
 */
public class SystemConfigEnum {

    public enum Group {
        BACK, SYSTEM, ORDER, REWARD
    }

    public enum Key {

        /**
         * 超管id
         */
        EMPLOYEE_SUPERMAN,
        /**
         * 阿里云OSS配置项
         */
        ALI_OSS,
        /**
         * 七牛云OSS配置项
         */
        QI_NIU_OSS,
        /**
         * 本地文件上传url前缀
         */
        LOCAL_UPLOAD_URL_PREFIX,
        /**
         * 邮件配置
         */
        EMAIL,

        /**
         * 订单超时分钟
         */
        ORDER_OVERTIME,

        /**
         * 回馈金比例
         */
        REWARD_RATIO,

        /**
         * 默认经纬度
         */
        DEFAULT_LONGITUDE_LATITUDE,

        /**
         * 后管默认密码
         */
        DEFAULT_EMPLOYEE_LOGIN_PASSWORD,

        /**
         * 店铺默认所属部门
         */
        DEFAULT_DEPARTMENT_FOR_SHOP,

        /**
         * 商家负责人角色ID
         */
        SHOP_MANAGER_ROLE,

        /**
         * 商家成员角色ID
         */
        SHOP_EMPLOYEE_ROLE,

        /**
         * 敏感词检测开启状态
         */
        SENSITIVE_WORD_DETECTION,

        /**
         * 商家结算周期配置
         */
        SHOP_STATEMENT_CYCLE_CONFIG

    }

}

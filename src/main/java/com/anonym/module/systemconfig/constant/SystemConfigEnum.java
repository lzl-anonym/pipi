package com.anonym.module.systemconfig.constant;

/**
 * 系统参数配置常量
 */
public class SystemConfigEnum {

    public enum Group {
        /**
         * 上传
         */
        UPLOAD,
    }

    public enum Key {

        EMPLOYEE_SUPERMAN,
        /**
         * 本地上传URL前缀
         */
        LOCAL_UPLOAD_URL_PREFIX,

        /**
         * 商服部门id
         */
        CUSTOMER_SERVICE_DEPT_ID,

        /**
         * 线下运营部门id
         */
        OFFLINE_OPERATION_ID,

        /**
         * 自营店铺部id
         */
        SELF_STORE_DEPARTMENT_ID,
        /**
         * 需要同步erp数据的部门id集合
         */
        SYNC_ERP_DATA_DEPARTMENT_ID;
    }

}

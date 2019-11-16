package com.anonym.module.log.operatelog.domain;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@TableName("t_employee_operate_log")
public class EmployeeOperateLogEntity extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 请求结果 0失败 1成功
     */
    private Boolean result;

    /**
     * 失败原因
     */
    private String failReason;

}

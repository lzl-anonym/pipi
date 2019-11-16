package com.anonym.module.admin.notice.domain.dto;

import lombok.Data;


@Data
public class NoticeReadCountDTO {

    /**
     * 员工id
     */
    private Long employeeId;

    /**
     * 已读消息数
     */
    private Integer readCount;

}

package com.anonym.module.admin.notice.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


@Data
@TableName("t_notice_receive_record")
public class NoticeReceiveRecordEntity extends BaseEntity {

    /**
     * 消息id
     */
    private Long noticeId;

    /**
     * 消息接收人
     */
    private Long employeeId;

}

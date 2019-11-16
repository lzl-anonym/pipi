package com.anonym.module.admin.notice.domain.entity;

import com.anonym.common.domain.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;


@Data
@TableName("t_notice")
public class NoticeEntity extends BaseEntity {

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息创建人
     */
    private Long createUser;

    /**
     * 发送状态
     */
    private Integer sendStatus;

    /**
     * 删除状态
     */
    private Integer deleted;

}

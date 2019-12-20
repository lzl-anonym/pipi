package com.anonym.module.message.domain.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

/**
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Data
@TableName("t_message")
public class MessageEntity {


    /**
     * 留言主键
     */
    @TableId(type = IdType.AUTO)
    private Long messageId;

    /**
     * 留言类型
     */
    private Integer messageType;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 图片（备用）
     */
    private String picture;

    /**
     * 留言人id（备用）
     */
    private Long userId;

    /**
     * 留言人名字（备用）
     */
    private String userName;

    /**
     * 删除状态  true删除  false不删除
     */
    private Boolean deleteFlag;


}

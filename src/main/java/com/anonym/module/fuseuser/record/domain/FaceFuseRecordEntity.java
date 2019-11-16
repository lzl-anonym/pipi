package com.anonym.module.fuseuser.record.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-11-16 16:00
 */
@Data
@TableName("t_face_fuse_record")
public class FaceFuseRecordEntity {

    @TableId(type = IdType.AUTO)
    private Integer faceFuseRecordId;

    /**
     * 用户id
     */
    private Integer faceFuseUserId;

    /**
     * 参与时间
     */
    private Date createTime;

}

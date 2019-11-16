package com.anonym.module.fuseuser.fuse.domian;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-11-16 16:44
 */
@Data
@TableName("t_face_fuse")
public class FaceFuseEntity {

    @TableId(type = IdType.AUTO)
    private Integer faceFuseId;

    /**
     * 用户id
     */
    private Integer faceFuseUserId;

    /**
     * 融合图的BASE64值
     */
    private String mergeImage;

    private Date updateTime;

    private Date createTime;
}

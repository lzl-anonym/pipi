package com.anonym.module.member.domian;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

/**
 * @author lizongliang
 * @date 2019-08-28 20:34
 */
@Data
@TableName("t_member")
public class MemberEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer memberId;

    /**
     * 服务项id
     */
    private Integer serviceItemId;

    /**
     * 手机
     */
    private String phone;

    /**
     * 名字
     */
    private String name;

    /**
     * 总次数
     */
    private Integer totalNum;

    /**
     * 剩余次数
     */
    private Integer unusedNum;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}

package com.anonym.module.file.domain.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName(value = "t_file")
public class FileEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件服务类
     * @see com.anonym.module.file.constant.FileServiceTypeEnum
     */
    private Integer serviceType;

    /**
     * 文件夹类型
     */
    private String folderType;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件key，用于文件下载
     */
    private String fileKey;

    /**
     * 创建人，即上传人
     */
    private Long createUser;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}


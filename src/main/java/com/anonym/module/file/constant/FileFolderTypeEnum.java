package com.anonym.module.file.constant;

import com.anonym.common.domain.BaseEnum;
import com.anonym.constant.CommonConst;

/**
 * 文件服务 文件夹位置类型枚举类
 */
public enum FileFolderTypeEnum implements BaseEnum {

    /**
     * 留言图片
     */
    MESSAGE(1, CommonConst.FileService.FOLDER_PUBLIC + "/message-pic/", "留言上传的图片");

    /**
     * 商城图片
     */
//    MARKET(2, CommonConst.FileService.FOLDER_PUBLIC + "/market/", "商城图片"),

    /**
     * 旅拍图片
     */
//    ARTICLE(3, CommonConst.FileService.FOLDER_PUBLIC + "/article/", "旅拍图片"),

    /**
     * 用户反馈图片
     */
//    FEEDBACK(4, CommonConst.FileService.FOLDER_PUBLIC + "/feedback/", "用户反馈图片"),

    /**
     * 路线图片
     */
//    ROUTE(5, CommonConst.FileService.FOLDER_PUBLIC + "/route/", "路线图片"),

    /**
     * 商品评价图片文件夹
     */
//    GOODS_ASSESS(6, CommonConst.FileService.FOLDER_PUBLIC + "/assess/", "商品评价图片文件夹");

    public static final String INFO = "1留言图片 ";

    private Integer value;

    private String folder;

    private String desc;

    FileFolderTypeEnum(Integer value, String folder, String desc) {
        this.value = value;
        this.folder = folder;
        this.desc = desc;
    }

    public String getFolder() {
        return folder;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}

package com.anonym.module.wechat.miniprogram.domain;
import lombok.Data;

/**
 * 微信 登录结果DTO类
 */
@Data
public class WeChatLoginResultDTO {

    private String openId;

    private String sessionKey;

    private String unionId;

}

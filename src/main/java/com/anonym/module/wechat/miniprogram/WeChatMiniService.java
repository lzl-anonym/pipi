package com.anonym.module.wechat.miniprogram;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.wechat.WeChatBaseService;
import com.anonym.module.wechat.WeChatConfig;
import com.anonym.module.wechat.miniprogram.domain.WeChatLoginResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 微信小程序 业务类
 */
@Slf4j
@Service
public class WeChatMiniService {

    @Autowired
    private WeChatBaseService weChatBaseService;

    /**
     * 微信小程序登录
     *
     * @param code
     * @return
     */
    public ResponseDTO<WeChatLoginResultDTO> loginByCode(String code) {

        ResponseDTO responseDTO = weChatBaseService.sendGetRequest(WeChatConfig.URL.MINI_LOGIN, code);

        if (!responseDTO.isSuccess()) {
            return responseDTO;
        }

        JSONObject jsonObject = (JSONObject) responseDTO.getData();

        String openId = jsonObject.getString("openid");
        String unionId = jsonObject.getString("unionid");
        String sessionKey = jsonObject.getString("session_key");

        // 返回登录信息
        WeChatLoginResultDTO loginResultDTO = new WeChatLoginResultDTO();
        loginResultDTO.setOpenId(openId);
        loginResultDTO.setSessionKey(sessionKey);
        loginResultDTO.setUnionId(unionId);

        return ResponseDTO.succData(loginResultDTO);
    }

    /**
     * 解密微信数据
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public static JSONObject decryptData(String encryptedData, String sessionKey, String iv) {
        String result;
        try {
            result = decrypt(encryptedData, sessionKey, iv, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject;
    }

    /**
     * 解密工具
     */
    private static String decrypt(String data, String sKey, String ivParameter, String encoding) throws Exception {
        byte[] raw = Base64.decodeBase64(sKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(ivParameter));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
        byte[] decodeBuffer = Base64.decodeBase64(data);
        byte[] original = cipher.doFinal(decodeBuffer);
        String originalString = new String(original, encoding);
        return originalString;
    }

    public static void main(String[] args) {
        JSONObject jsonObject = decryptData("xoUONzirc+SvUHVKl5zw4zzwsCf36+yR3o7S4C0B1jkmkXWQoZKO24Li22J3/+cgCCo4qCjjZzWSD1ck5VKTGjZ/+VKX0cPgFG91hSsvqpqla1gYZCQMXVtCX809T9ewgBH4eBMHvC6J6UOsh8R5cNM35wVEQVCeqpEtioXzkdwJvjCWhZ0YSiRfXE3qiDJiNxozDKkWWg20KSvPo7UGIQ==", "ugbl8Ek61zCvPEpQbu7c3Q==", "/Kuxr+quxIMK3tWD74Ig+w==");
        System.out.println(jsonObject);
    }
}

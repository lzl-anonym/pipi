package com.anonym.module.user.basic;

import com.alibaba.fastjson.JSONObject;
import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.user.UserResponseCodeConst;
import com.anonym.module.user.basic.domain.UserBindWeChatPhone;
import com.anonym.module.user.basic.domain.UserEntity;
import com.anonym.module.user.basic.domain.UserInfoUpdateDTO;
import com.anonym.module.wechat.miniprogram.WeChatMiniService;
import com.anonym.utils.SmartDigestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户通用业务类
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private SmsService smsService;

    /**
     * 根据微信open id 获取用户信息
     *
     * @param openId
     * @return
     */
    public UserEntity getUserByOpenId(String openId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setWeChatOpenId(openId);
        userEntity = userDao.selectOne(userEntity);
        return userEntity;
    }

    /**
     * 加密 密码
     *
     * @param pwd
     * @return
     */
    public static String encryptPassword(String pwd) {
        return SmartDigestUtil.encryptPassword(CommonConst.Password.SALT_FORMAT, pwd);
    }

    /**
     * 用户手动绑定手机号
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateUserInfo(Long userId, UserInfoUpdateDTO updateDTO) {
        UserEntity userEntity = userDao.selectById(userId);
        if (Objects.isNull(userEntity)) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        // 校验手机号是否已存在
        UserEntity userByPhone = userDao.selectUser(userId, updateDTO.getPhone());
        if (Objects.nonNull(userByPhone)) {
            return ResponseDTO.wrap(UserResponseCodeConst.PHONE_EXIST);
        }
        // 校验验证码
        // TODO: 2019-11-16
//        ResponseDTO<String> responseDTO = smsService.checkVerifyCode(updateDTO.getPhone(), updateDTO.getVerificationCode(), SmsTypeEnum.COMMON_VERIFY_CODE);
//        if (!responseDTO.isSuccess()) {
//            return responseDTO;
//        }
        userEntity.setPhone(updateDTO.getPhone());
        userDao.updateById(userEntity);
        return ResponseDTO.succData(updateDTO.getPhone());
    }

    /**
     * 用户绑定微信手机号
     *
     * @param weChatPhone
     * @return
     */
    public ResponseDTO<String> bindWeChatPhone(UserBindWeChatPhone weChatPhone) {

        /**
         * 获取 用户的微信 session_key ，然后数据，得到手机号码
         * 更新用户手机号码信息
         * 返回前端手机号码
         */
        Long userId = weChatPhone.getUserId();
        UserEntity userEntity = userDao.selectById(userId);
        String weChatSessionKey = userEntity.getWeChatSessionKey();
        JSONObject jsonObject = WeChatMiniService.decryptData(weChatPhone.getEncryptedData(), weChatSessionKey, weChatPhone.getIv());
        if (null == jsonObject) {
            return ResponseDTO.wrap(UserResponseCodeConst.BIND_PHONE_ERROR);
        }
        String phone = jsonObject.getString("phoneNumber");
        if (StringUtils.isBlank(phone)) {
            return ResponseDTO.wrap(UserResponseCodeConst.BIND_PHONE_ERROR);
        }
        userEntity.setPhone(phone);
        userDao.updateUser(userEntity);
        return ResponseDTO.succData(phone);
    }

}

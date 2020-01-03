package com.anonym.module.user.basic;

import com.anonym.constant.CommonConst;
import com.anonym.module.user.basic.domain.UserEntity;
import com.anonym.utils.SmartDigestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户通用业务类
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;


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


}

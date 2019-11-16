package com.anonym.module.user.basic;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.user.UserResponseCodeConst;
import com.anonym.module.user.basic.domain.UserAdminQueryDTO;
import com.anonym.module.user.basic.domain.UserAdminVO;
import com.anonym.module.user.basic.domain.UserEntity;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 后管业务类
 */
@Service
public class UserAdminService {

    @Autowired
    private UserDao userDao;

    /**
     * 后管分页查询用户列表
     *
     * @param query
     * @return
     */
    public ResponseDTO<PageResultDTO<UserAdminVO>> query(UserAdminQueryDTO query) {
        Page page = SmartPageUtil.convert2PageQuery(query);
        List<UserEntity> entityList = userDao.query(page, query);
        PageResultDTO<UserAdminVO> pageResult = SmartPageUtil.convert2PageResult(page, entityList, UserAdminVO.class);
        return ResponseDTO.succData(pageResult);
    }

    /**
     * 更新用户禁用状态
     *
     * @param userId
     * @param disabled
     * @return
     */
    public ResponseDTO<String> updateDisabled(Long userId, Boolean disabled) {
        int count = userDao.countByUserId(userId);
        if (count == 0) {
            return ResponseDTO.wrap(UserResponseCodeConst.USER_NOT_EXISTS);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setDisabledFlag(disabled);
        userDao.updateUser(userEntity);
        return ResponseDTO.succ();
    }

}

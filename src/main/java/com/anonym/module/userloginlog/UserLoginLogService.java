package com.anonym.module.userloginlog;

import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogQueryDTO;
import com.anonym.module.userloginlog.domain.UserLoginLogEntity;
import com.anonym.utils.PaginationUtil;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [ 用户登录日志 ]
 */
@Service
public class UserLoginLogService {

    @Autowired
    private UserLoginLogDao userLoginLogDao;


    /**
     * @description 分页查询
     * @date 2019-05-15 10:25:21
     */
    public ResponseDTO<PageInfoDTO<UserLoginLogDTO>> queryByPage(UserLoginLogQueryDTO queryDTO) {
        Page page = PaginationUtil.convert2PageQueryInfo(queryDTO);
        List<UserLoginLogEntity> entities = userLoginLogDao.queryByPage(page, queryDTO);
        List<UserLoginLogDTO> dtoList = SmartBeanUtil.copyList(entities, UserLoginLogDTO.class);
        page.setRecords(dtoList);
        PageInfoDTO<UserLoginLogDTO> pageInfoDTO = PaginationUtil.convert2PageInfoDTO(page);
        return ResponseDTO.succData(pageInfoDTO);
    }

    /**
     * @description 删除
     * @date 2019-05-15 10:25:21
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id) {
        userLoginLogDao.deleteById(id);
        return ResponseDTO.succ();
    }

}

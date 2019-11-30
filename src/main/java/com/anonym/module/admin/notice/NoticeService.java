package com.anonym.module.admin.notice;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.PageBaseDTO;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.admin.notice.dao.NoticeDao;
import com.anonym.module.admin.notice.dao.NoticeReceiveRecordDao;
import com.anonym.module.admin.notice.domain.dto.*;
import com.anonym.module.admin.notice.domain.entity.NoticeEntity;
import com.anonym.module.admin.notice.domain.entity.NoticeReceiveRecordEntity;
import com.anonym.module.websocket.WebSocketAdminServer;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeReceiveRecordDao noticeReceiveRecordDao;

    @Autowired
    private NoticeManage noticeManage;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<PageResultDTO<NoticeVO>> queryByPage(NoticeQueryDTO queryDTO) {
        queryDTO.setDeleted(JudgeEnum.NO.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<NoticeVO> dtoList = noticeDao.queryByPage(page, queryDTO);
        page.setRecords(dtoList);
        PageResultDTO<NoticeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 获取当前登录人的消息列表
     *
     * @param queryDTO
     * @param requestToken
     * @return
     */
    public ResponseDTO<PageResultDTO<NoticeReceiveDTO>> queryReceiveByPage(NoticeReceiveQueryDTO queryDTO, LoginTokenDTO requestToken) {
        queryDTO.setEmployeeId(requestToken.getId());
        queryDTO.setSendStatus(JudgeEnum.YES.getValue());
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<NoticeReceiveDTO> dtoList = noticeDao.queryReceiveByPage(page, queryDTO);
        dtoList.forEach(e -> {
            if (e.getReceiveTime() == null) {
                e.setReadStatus(JudgeEnum.NO.getValue());
            } else {
                e.setReadStatus(JudgeEnum.YES.getValue());
            }
        });
        page.setRecords(dtoList);
        PageResultDTO<NoticeReceiveDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 获取我的未读消息
     *
     * @param queryDTO
     * @param requestToken
     * @return
     */
    public ResponseDTO<PageResultDTO<NoticeVO>> queryUnreadByPage(PageBaseDTO queryDTO, LoginTokenDTO requestToken) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<NoticeVO> dtoList = noticeDao.queryUnreadByPage(page, requestToken.getId(), JudgeEnum.YES.getValue());
        page.setRecords(dtoList);
        PageResultDTO<NoticeVO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> add(NoticeAddDTO addDTO, LoginTokenDTO requestToken) {
        NoticeEntity entity = SmartBeanUtil.copy(addDTO, NoticeEntity.class);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setCreateUser(requestToken.getId());
        entity.setSendStatus(JudgeEnum.NO.getValue());
        entity.setDeleted(JudgeEnum.NO.getValue());
        noticeDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-07-11 16:19:48
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(NoticeUpdateDTO updateDTO) {
        NoticeEntity entity = noticeDao.selectById(updateDTO.getId());
        if (entity == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        if (JudgeEnum.YES.getValue().equals(entity.getSendStatus())) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "此系统通知已发送无法修改");
        }
        noticeManage.update(entity, updateDTO);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<String> delete(Long id) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        noticeManage.delete(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-07-11 16:19:48
     */
    public ResponseDTO<NoticeDetailVO> detail(Long id) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);
        return ResponseDTO.succData(noticeDTO);
    }

    /**
     * 获取某人的未读消息数
     *
     * @param employeeId
     * @return
     */
    private Integer getUnreadCount(Long employeeId) {
        return noticeDao.noticeUnreadCount(employeeId, JudgeEnum.YES.getValue());
    }

    /**
     * 发送给所有在线用户未读消息数
     *
     * @param id
     * @param requestToken
     * @return
     */
    public ResponseDTO<NoticeDetailVO> send(Long id, LoginTokenDTO requestToken) {
        NoticeEntity entity = noticeDao.selectById(id);
        if (entity == null) {
            return ResponseDTO.wrapMsg(ResponseCodeConst.ERROR_PARAM, "此系统通知不存在");
        }
        noticeManage.send(entity, requestToken);
        this.sendMessage(requestToken);
        return ResponseDTO.succ();
    }

    /**
     * 发送系统通知 ，发送人不进行接收,需再事务外调用 以防止数据隔离级别不同造成未读消息数异常
     *
     * @param requestToken
     */
    private void sendMessage(LoginTokenDTO requestToken) {
        List<Long> onLineEmployeeIds = WebSocketAdminServer.getOnLineUserList();
        if (CollectionUtils.isEmpty(onLineEmployeeIds)) {
            return;
        }
        //在线用户已读消息数
        Map<Long, Integer> readCountMap = new HashMap<>();
        List<NoticeReadCountDTO> readCountList = noticeDao.readCount(onLineEmployeeIds);
        if (CollectionUtils.isNotEmpty(readCountList)) {
            readCountMap = readCountList.stream().collect(Collectors.toMap(NoticeReadCountDTO::getEmployeeId, NoticeReadCountDTO::getReadCount));
        }
        //已发送消息数
        Integer noticeCount = noticeDao.noticeCount(JudgeEnum.YES.getValue());
        for (Long employeeId : onLineEmployeeIds) {
            Integer readCount = readCountMap.get(employeeId) == null ? 0 : readCountMap.get(employeeId);
            Integer unReadCount = noticeCount - readCount;
            if (!requestToken.getId().equals(employeeId)) {
                WebSocketAdminServer.sendMsg(employeeId, unReadCount.toString());
            }
        }
    }

    /**
     * 读取消息
     *
     * @param id
     * @param requestToken
     * @return
     */
    public ResponseDTO<NoticeDetailVO> read(Long id, LoginTokenDTO requestToken) {
        NoticeDetailVO noticeDTO = noticeDao.detail(id);

        NoticeReceiveRecordEntity recordEntity = noticeReceiveRecordDao.selectByEmployeeAndNotice(requestToken.getId(), id);
        if (recordEntity != null) {
            return ResponseDTO.succData(noticeDTO);
        }
        noticeManage.saveReadRecord(id, requestToken);
        this.sendMessage(requestToken);
        return ResponseDTO.succData(noticeDTO);
    }
}
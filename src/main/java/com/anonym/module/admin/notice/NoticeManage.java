package com.anonym.module.admin.notice;

import com.anonym.common.constant.JudgeEnum;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.admin.notice.dao.NoticeDao;
import com.anonym.module.admin.notice.dao.NoticeReceiveRecordDao;
import com.anonym.module.admin.notice.domain.dto.NoticeUpdateDTO;
import com.anonym.module.admin.notice.domain.entity.NoticeEntity;
import com.anonym.module.admin.notice.domain.entity.NoticeReceiveRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class NoticeManage {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private NoticeReceiveRecordDao noticeReceiveRecordDao;

    /**
     * 发送消息
     *
     * @param entity
     * @param requestToken
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(NoticeEntity entity, LoginTokenDTO requestToken) {

        entity.setSendStatus(JudgeEnum.YES.getValue());
        noticeDao.updateById(entity);
        //默认发件人 已读此消息
        NoticeReceiveRecordEntity recordEntity = new NoticeReceiveRecordEntity();
        recordEntity.setEmployeeId(requestToken.getId());
        recordEntity.setNoticeId(entity.getId());
        recordEntity.setCreateTime(new Date());
        recordEntity.setUpdateTime(new Date());
        noticeReceiveRecordDao.insert(recordEntity);
    }

    /**
     * 保存读取记录
     *
     * @param noticeId
     * @param requestToken
     */
    public void saveReadRecord(Long noticeId, LoginTokenDTO requestToken) {
        NoticeReceiveRecordEntity recordEntity = new NoticeReceiveRecordEntity();
        recordEntity.setEmployeeId(requestToken.getId());
        recordEntity.setNoticeId(noticeId);
        recordEntity.setCreateTime(new Date());
        recordEntity.setUpdateTime(new Date());
        noticeReceiveRecordDao.insert(recordEntity);
    }

    /**
     * 消息删除
     *
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(NoticeEntity entity) {
        if (JudgeEnum.YES.getValue().equals(entity.getSendStatus())) {
            //消息已发送 执行逻辑删除
            noticeDao.logicDeleteById(entity.getId(), JudgeEnum.YES.getValue());
        } else {
            //消息未发送 执行真实删除
            noticeDao.deleteById(entity.getId());
        }
    }

    /**
     * 更新消息
     *
     * @param entity
     * @param updateDTO
     */
    public void update(NoticeEntity entity, NoticeUpdateDTO updateDTO) {
        entity.setTitle(updateDTO.getTitle());
        entity.setContent(updateDTO.getContent());
        entity.setSendStatus(JudgeEnum.NO.getValue());
        entity.setDeleted(JudgeEnum.NO.getValue());
        noticeDao.updateById(entity);
    }
}

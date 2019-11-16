package com.anonym.module.admin.notice.dao;

import com.anonym.module.admin.notice.domain.entity.NoticeReceiveRecordEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface NoticeReceiveRecordDao extends BaseMapper<NoticeReceiveRecordEntity> {

    /**
     * 批量删除
     *
     * @param noticeId
     * @return
     */
    void deleteByNoticeId(@Param("noticeId") Long noticeId);

    /**
     * 批量插入
     *
     * @param rolePrivilegeList
     */
    void batchInsert(List<NoticeReceiveRecordEntity> rolePrivilegeList);

    /**
     * 根据员工和系统通知获取读取记录
     *
     * @param employeeId
     * @param noticeId
     * @return
     */
    NoticeReceiveRecordEntity selectByEmployeeAndNotice(@Param("employeeId") Long employeeId, @Param("noticeId") Long noticeId);
}

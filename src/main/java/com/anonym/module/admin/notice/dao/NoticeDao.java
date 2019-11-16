package com.anonym.module.admin.notice.dao;

import com.anonym.module.admin.notice.domain.dto.*;
import com.anonym.module.admin.notice.domain.entity.NoticeEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface NoticeDao extends BaseMapper<NoticeEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return NoticeEntity
     */
    List<NoticeVO> queryByPage(Pagination page, @Param("queryDTO") NoticeQueryDTO queryDTO);

    /**
     * 获取某人的未读消息
     *
     * @param page
     * @param employeeId
     * @return
     */
    List<NoticeVO> queryUnreadByPage(Pagination page, @Param("employeeId") Long employeeId, @Param("sendStatus") Integer sendStatus);

    /**
     * 获取
     *
     * @param page
     * @param queryDTO
     * @return
     */
    List<NoticeReceiveDTO> queryReceiveByPage(Pagination page, @Param("queryDTO") NoticeReceiveQueryDTO queryDTO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    NoticeDetailVO detail(@Param("id") Long id);

    /**
     * 根据id删除 逻辑删除
     *
     * @param id
     * @param deletedFlag
     */
    void logicDeleteById(@Param("id") Long id, @Param("deletedFlag") Integer deletedFlag);

    /**
     * 批量逻辑删除
     *
     * @param idList
     * @param deletedFlag
     * @return
     */
    void logicDeleteByIds(@Param("idList") List<Long> idList, @Param("deletedFlag") Integer deletedFlag);

    /**
     * 获取消息总数
     *
     * @return
     */
    Integer noticeCount(@Param("sendStatus") Integer sendStatus);

    /**
     * 获取已读消息数
     *
     * @param employeeIds
     * @return
     */
    List<NoticeReadCountDTO> readCount(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 获取某人的未读消息数
     *
     * @param employeeId
     * @param sendStatus
     * @return
     */
    Integer noticeUnreadCount(@Param("employeeId") Long employeeId, @Param("sendStatus") Integer sendStatus);

}

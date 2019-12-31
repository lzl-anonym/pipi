package com.anonym.module.message;

import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.entity.MessageEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 留言持久层
 *
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Mapper
@Component
public interface MessageDao extends BaseMapper<MessageEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return MessageEntity
     */
    List<MessageEntity> queryByPage(Pagination page, @Param("queryDTO") MessageAppQueryDTO queryDTO);

    /**
     * 批量删除
     *
     * @param idList
     * @param deleteFlag
     * @return
     */
    Integer batchUpdateDelete(@Param("idList") List<Long> idList, @Param("deleteFlag") Boolean deleteFlag);
}

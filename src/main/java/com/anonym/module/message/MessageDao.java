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
     * 根据id删除
     *
     * @param id
     * @return
     */
    void deleteById(@Param("id") Long id);

    /**
     * 批量删除
     *
     * @param idList
     * @return
     */
    void deleteByIds(@Param("idList") List<Long> idList);
}

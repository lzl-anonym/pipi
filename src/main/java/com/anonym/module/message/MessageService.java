package com.anonym.module.message;

import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.message.domain.dto.MessageAddAppDTO;
import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.dto.MessageBatchDeleteAdminDTO;
import com.anonym.module.message.domain.entity.MessageEntity;
import com.anonym.module.message.domain.vo.MessageAppVO;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 留言业务层
 *
 * @author lizongliang
 * @date 2019-12-20 18:48
 */
@Service
public class MessageService {

    @Autowired
    private MessageDao messageDao;


    /**
     * 分页查询留言（app端+后管）
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<MessageAppVO>> queryByPage(MessageAppQueryDTO queryDTO) {
        queryDTO.setDeleteFlag(false);
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<MessageEntity> messageEntities = messageDao.queryByPage(page, queryDTO);
        PageResultDTO<MessageAppVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, messageEntities, MessageAppVO.class);
        return ResponseDTO.succData(pageResultDTO);

    }


    /**
     * 添加留言（app端）
     *
     * @param addDTO
     * @return
     */
    public ResponseDTO<String> add(MessageAddAppDTO addDTO) {
        MessageEntity entity = SmartBeanUtil.copy(addDTO, MessageEntity.class);
        messageDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * 批量删除留言（后管）
     *
     * @param deleteDTO
     * @return
     */
    public ResponseDTO<String> batchDelete(MessageBatchDeleteAdminDTO deleteDTO) {
        if (CollectionUtils.isEmpty(deleteDTO.getMessageIdList())) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        messageDao.batchUpdateDelete(deleteDTO.getMessageIdList(), true);
        return ResponseDTO.succ();
    }

    /**
     * 留言详情（app端+后管）
     *
     * @param messageId
     * @return
     */
    public ResponseDTO<MessageAppVO> detail(Long messageId) {
        MessageEntity entity = messageDao.selectById(messageId);
        if (null == entity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        MessageAppVO dto = SmartBeanUtil.copy(entity, MessageAppVO.class);
        return ResponseDTO.succData(dto);
    }
}

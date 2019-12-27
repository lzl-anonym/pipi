package com.anonym.module.message;

import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.message.domain.dto.MessageAddAppDTO;
import com.anonym.module.message.domain.dto.MessageAppQueryDTO;
import com.anonym.module.message.domain.dto.MessageBaseDTO;
import com.anonym.module.message.domain.entity.MessageEntity;
import com.anonym.module.message.domain.vo.MessageAppVO;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * f分页查询留言（app端）
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
     * @author lizongliang
     * @description 编辑
     * @date 2019-12-20 18:43:17
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(MessageBaseDTO updateDTO) {
        MessageEntity entity = SmartBeanUtil.copy(updateDTO, MessageEntity.class);
        messageDao.updateById(entity);
        return ResponseDTO.succ();
    }


    /**
     * 删除留言
     *
     * @param messageId
     * @return
     */
    public ResponseDTO<String> delete(Long messageId) {
        MessageEntity messageEntity = messageDao.selectById(messageId);
        if (null == messageEntity) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        messageEntity.setDeleteFlag(true);
        messageDao.updateById(messageEntity);
        return ResponseDTO.succ();
    }

    /**
     * 留言详情
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

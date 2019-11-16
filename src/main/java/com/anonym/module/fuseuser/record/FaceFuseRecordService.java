package com.anonym.module.fuseuser.record;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordEntity;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordQueryDTO;
import com.anonym.module.fuseuser.record.domain.FaceFuseRecordVO;
import com.anonym.utils.SmartPageUtil;
import com.anonym.utils.SmartRequestTokenUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizongliang
 * @date 2019-11-16 16:03
 */
@Service
public class FaceFuseRecordService {

    @Autowired
    private FaceFuseRecordDao faceFuseRecordDao;

    /**
     * 添加参与记录  内部调用
     *
     * @return
     */
    public ResponseDTO addRecord() {
        Long userId = SmartRequestTokenUtil.getRequestUserId();
        FaceFuseRecordEntity addEntity = new FaceFuseRecordEntity();
        addEntity.setFaceFuseUserId(userId.intValue());
        faceFuseRecordDao.insert(addEntity);
        return ResponseDTO.succ();
    }

    /**
     * 后管分页查询 参与记录
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<FaceFuseRecordVO>> queryByPage(FaceFuseRecordQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<FaceFuseRecordVO> recordVOS = faceFuseRecordDao.queryByPage(page, queryDTO);
        PageResultDTO<FaceFuseRecordVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, recordVOS);
        return ResponseDTO.succData(pageResultDTO);
    }
}

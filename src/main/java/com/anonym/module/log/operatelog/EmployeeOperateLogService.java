package com.anonym.module.log.operatelog;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogDTO;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogEntity;
import com.anonym.module.log.operatelog.domain.EmployeeOperateLogQueryDTO;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class EmployeeOperateLogService {

    @Autowired
    private EmployeeOperateLogDao userOperateLogDao;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<PageResultDTO<EmployeeOperateLogDTO>> queryByPage(EmployeeOperateLogQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EmployeeOperateLogEntity> entities = userOperateLogDao.queryByPage(page, queryDTO);
        List<EmployeeOperateLogDTO> dtoList = SmartBeanUtil.copyList(entities, EmployeeOperateLogDTO.class);
        page.setRecords(dtoList);
        PageResultDTO<EmployeeOperateLogDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<String> add(EmployeeOperateLogDTO addDTO) {
        EmployeeOperateLogEntity entity = SmartBeanUtil.copy(addDTO, EmployeeOperateLogEntity.class);
        userOperateLogDao.insert(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-05-15 11:32:14
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(EmployeeOperateLogDTO updateDTO) {
        EmployeeOperateLogEntity entity = SmartBeanUtil.copy(updateDTO, EmployeeOperateLogEntity.class);
        userOperateLogDao.updateById(entity);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-05-15 11:32:14
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id) {
        userOperateLogDao.deleteById(id);
        return ResponseDTO.succ();
    }

    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-05-15 11:32:14
     */
    public ResponseDTO<EmployeeOperateLogDTO> detail(Long id) {
        EmployeeOperateLogEntity entity = userOperateLogDao.selectById(id);
        EmployeeOperateLogDTO dto = SmartBeanUtil.copy(entity, EmployeeOperateLogDTO.class);
        return ResponseDTO.succData(dto);
    }
}

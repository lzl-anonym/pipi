package com.anonym.module.log.loginlog;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.module.admin.employee.basic.EmployeeService;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeQueryDTO;
import com.anonym.module.admin.employee.basic.domain.vo.EmployeeVO;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogDTO;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogEntity;
import com.anonym.module.log.loginlog.domain.EmployeeLoginLogQueryDTO;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class EmployeeLoginLogService {

    @Autowired
    private EmployeeLoginLogDao userLoginLogDao;

    @Autowired
    private EmployeeService employeeService;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-15 10:25:21
     */
    public ResponseDTO<PageResultDTO<EmployeeLoginLogDTO>> queryByPage(EmployeeLoginLogQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<EmployeeLoginLogEntity> entities = userLoginLogDao.queryByPage(page, queryDTO);
        List<EmployeeLoginLogDTO> dtoList = SmartBeanUtil.copyList(entities, EmployeeLoginLogDTO.class);
        page.setRecords(dtoList);
        PageResultDTO<EmployeeLoginLogDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 删除
     * @date 2019-05-15 10:25:21
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long id) {
        userLoginLogDao.deleteById(id);
        return ResponseDTO.succ();
    }

    /**
     * 查询员工在线状态
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<EmployeeVO>> queryUserOnLine(EmployeeQueryDTO queryDTO) {

        return null;
    }

}

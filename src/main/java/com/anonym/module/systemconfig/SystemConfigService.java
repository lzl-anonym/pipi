package com.anonym.module.systemconfig;

import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.exception.SmartBusinessException;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.constant.SystemConfigResponseCodeConst;
import com.anonym.module.systemconfig.domain.dto.SystemConfigDTO;
import com.anonym.module.systemconfig.domain.dto.SystemConfigQueryDTO;
import com.anonym.module.systemconfig.domain.dto.SystemConfigVO;
import com.anonym.module.systemconfig.domain.entity.SystemConfigEntity;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统配置业务类
 */
@Slf4j
@Service
public class SystemConfigService {

    /**
     * 系统配置缓存
     */
    private ConcurrentHashMap<String, SystemConfigEntity> systemConfigMap = new ConcurrentHashMap<>();

    @Autowired
    private SystemConfigDao systemConfigDao;

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void initSystemConfigCache() {
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList();
        if (CollectionUtils.isEmpty(entityList)) {
            return;
        }
        systemConfigMap.clear();
        entityList.forEach(entity -> this.systemConfigMap.put(entity.getConfigKey().toLowerCase(), entity));
    }

    /**
     *
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageResultDTO<SystemConfigVO>> getSystemConfigPage(SystemConfigQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2PageQuery(queryDTO);
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList(page, queryDTO);
        PageResultDTO<SystemConfigVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, entityList, SystemConfigVO.class);
        return ResponseDTO.succData(pageResultDTO);
    }

    /**
     * 根据参数key获得一条数据（数据库）
     *
     * @param configKey
     * @return
     */
    public ResponseDTO<SystemConfigVO> selectByKey(String configKey) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configKey);
        if (entity == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        SystemConfigVO configDTO = SmartBeanUtil.copy(entity, SystemConfigVO.class);
        return ResponseDTO.succData(configDTO);
    }


    public SystemConfigDTO getCacheByKey(SystemConfigEnum.Key key) {
        SystemConfigEntity entity = this.systemConfigMap.get(key.name().toLowerCase());
        if (entity == null) {
            throw new SmartBusinessException("缺少系统配置[" + key.name() + "]");
        }
        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
    }


    /**
     * 根据分组名称 获取获取系统设置
     *
     * @param group
     * @return
     */
    public ResponseDTO<List<SystemConfigVO>> getListByGroup(String group) {

        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group);
        if (CollectionUtils.isEmpty(entityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<SystemConfigVO> systemConfigList = SmartBeanUtil.copyList(entityList, SystemConfigVO.class);
        return ResponseDTO.succData(systemConfigList);
    }


}

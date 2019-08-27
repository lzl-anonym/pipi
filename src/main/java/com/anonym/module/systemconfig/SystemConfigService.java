package com.anonym.module.systemconfig;

import com.alibaba.fastjson.JSON;
import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.domain.PageInfoDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.exception.SmartBusinessException;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.constant.SystemConfigResponseCodeConst;
import com.anonym.module.systemconfig.domain.*;
import com.anonym.utils.PaginationUtil;
import com.anonym.utils.SmartBeanUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 系统配置业务类
 *
 */
@Service
public class SystemConfigService {

    /**
     * 系统配置缓存
     */
    private ConcurrentHashMap<String, SystemConfigEntity> systemConfigMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, SystemConfigEntity> systemConfigByIdMap = new ConcurrentHashMap<>();

    @Autowired
    private SystemConfigDao systemConfigDao;

    /**
     * 初始化系统设置缓存
     */
    @PostConstruct
    private void initSystemConfigCache() {
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList();
        if (CollectionUtils.isNotEmpty(entityList)) {
            systemConfigMap.clear();
            systemConfigByIdMap.clear();
            entityList.forEach(entity -> {
                this.systemConfigMap.put(entity.getConfigKey().toLowerCase(), entity);
                this.systemConfigByIdMap.put(entity.getId().toString(), entity);
            });
        }
    }

    /**
     * 刷新系统设置缓存
     */
    public void refreshSystemConfigCache() {
        this.initSystemConfigCache();
    }

    /**
     * 获取系统设置缓存map
     *
     * @return
     */
    public ConcurrentHashMap<String, SystemConfigEntity> getSystemConfigMap() {
        ConcurrentHashMap<String, SystemConfigEntity> configMap = new ConcurrentHashMap<>();
        configMap.putAll(this.systemConfigMap);
        return configMap;
    }

    /**
     * 分页获取系统配置
     *
     * @param queryDTO
     * @return
     */
    public ResponseDTO<PageInfoDTO<SystemConfigDTO>> getSystemConfigPage(SystemConfigQueryDTO queryDTO) {
        Page page = PaginationUtil.convert2PageQueryInfo(queryDTO);
        List<SystemConfigEntity> entityList = systemConfigDao.selectSystemSettingList(page, queryDTO);
        page.setRecords(entityList.stream().map(e -> SmartBeanUtil.copy(e, SystemConfigDTO.class)).collect(Collectors.toList()));
        PageInfoDTO<SystemConfigDTO> pageInfoDTO = PaginationUtil.convert2PageInfoDTO(page);
        return ResponseDTO.succData(pageInfoDTO);
    }

    /**
     * 根据参数key获得一条数据（数据库）
     *
     * @param configKey
     * @return
     */
    public ResponseDTO<SystemConfigDTO> selectByKey(String configKey) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configKey);
        if (entity == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        SystemConfigDTO configDTO = SmartBeanUtil.copy(entity, SystemConfigDTO.class);
        return ResponseDTO.succData(configDTO);
    }

    public <T> T selectByKey2Obj(String configKey, Class<T> clazz) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configKey);
        if (entity == null) {
            return null;
        }
        SystemConfigDTO configDTO = SmartBeanUtil.copy(entity, SystemConfigDTO.class);
        String configValue = configDTO.getConfigValue();
        if (StringUtils.isEmpty(configValue)) {
            return null;
        }
        T obj = JSON.parseObject(configValue, clazz);
        return obj;
    }

    /**
     * 添加系统配置
     *
     * @param configAddDTO
     * @return
     */
    public ResponseDTO<String> addSystemConfig(SystemConfigAddDTO configAddDTO) {
        SystemConfigEntity entity = systemConfigDao.getByKey(configAddDTO.getConfigKey());
        if (entity != null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.ALREADY_EXIST);
        }
        SystemConfigEntity addEntity = SmartBeanUtil.copy(configAddDTO, SystemConfigEntity.class);
        addEntity.setIsUsing(JudgeEnum.YES.getValue());
        addEntity.setCreateTime(new Date());
        addEntity.setUpdateTime(new Date());
        systemConfigDao.insert(addEntity);
        //刷新缓存
        this.refreshSystemConfigCache();
        return ResponseDTO.succ();
    }

    /**
     * 更新系统配置
     *
     * @param updateDTO
     * @return
     */
    public ResponseDTO<String> updateSystemConfig(SystemConfigUpdateDTO updateDTO) {
        SystemConfigEntity entity = systemConfigDao.selectById(updateDTO.getId());
        //系统配置不存在
        if (entity == null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.NOT_EXIST);
        }
        SystemConfigEntity alreadyEntity = systemConfigDao.getByKeyExcludeId(updateDTO.getConfigKey(), updateDTO.getId());
        if (alreadyEntity != null) {
            return ResponseDTO.wrap(SystemConfigResponseCodeConst.ALREADY_EXIST);
        }

        entity = SmartBeanUtil.copy(updateDTO, SystemConfigEntity.class);
        entity.setUpdateTime(new Date());
        systemConfigDao.updateById(entity);

        //刷新缓存
        this.refreshSystemConfigCache();
        return ResponseDTO.succ();
    }

    public SystemConfigDTO getCacheByKey(SystemConfigEnum.Key key) {
        SystemConfigEntity entity = this.systemConfigMap.get(key.name().toLowerCase());
        if (entity == null) {
            throw new SmartBusinessException("缺少系统配置[" + key.name() + "]");
        }
        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
    }

    public SystemConfigDTO getCacheByKey(SystemConfigInterface key) {
        SystemConfigEntity entity = this.systemConfigMap.get(key.getConfigKey());
        if (entity == null) {
            throw new SmartBusinessException("缺少系统配置[" + key.getConfigKey() + "]");
        }
        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
    }

    public ResponseDTO<List<SystemConfigDTO>> getListByGroup(String group) {

        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group);
        if (CollectionUtils.isEmpty(entityList)) {
            return ResponseDTO.succData(Lists.newArrayList());
        }
        List<SystemConfigDTO> systemConfigList = entityList.stream().map(e -> SmartBeanUtil.copy(e, SystemConfigDTO.class)).collect(Collectors.toList());
        return ResponseDTO.succData(systemConfigList);
    }

    public List<SystemConfigDTO> getListByGroup(SystemConfigEnum.Group group) {
        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group.name());
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<SystemConfigDTO> systemConfigList = entityList.stream().map(e -> SmartBeanUtil.copy(e, SystemConfigDTO.class)).collect(Collectors.toList());
        return systemConfigList;
    }

}

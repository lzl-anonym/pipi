package com.anonym.module.systemconfig;

import com.alibaba.fastjson.JSON;
import com.anonym.common.constant.JudgeEnum;
import com.anonym.common.constant.ResponseCodeConst;
import com.anonym.common.domain.PageResultDTO;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.exception.SmartBusinessException;
import com.anonym.module.systemconfig.constant.SystemConfigEnum;
import com.anonym.module.systemconfig.constant.SystemConfigResponseCodeConst;
import com.anonym.module.systemconfig.domain.dto.*;
import com.anonym.module.systemconfig.domain.entity.SystemConfigEntity;
import com.anonym.utils.SmartBeanUtil;
import com.anonym.utils.SmartPageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        log.info("系统配置缓存初始化完毕:{}", systemConfigMap.size());
    }

    /**
     * 分页获取系统配置
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

    /**
     * 根据参数key获得一条数据 并转换为 对象
     *
     * @param configKey
     * @param clazz
     * @param <T>
     * @return
     */
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

    public SystemConfigDTO getCacheByKey(SystemConfigEnum.Key key) {
        SystemConfigEntity entity = this.systemConfigMap.get(key.name().toLowerCase());
        if (entity == null) {
            throw new SmartBusinessException("缺少系统配置[" + key.name() + "]");
        }
        return SmartBeanUtil.copy(entity, SystemConfigDTO.class);
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
        systemConfigDao.insert(addEntity);
        //刷新缓存
        this.initSystemConfigCache();
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
        systemConfigDao.updateById(entity);

        //刷新缓存
        this.initSystemConfigCache();
        return ResponseDTO.succ();
    }

    /**
     * 更新系统配置
     *
     * @param key
     * @param value
     * @return
     */
    public ResponseDTO<String> updateValueByKey(SystemConfigEnum.Key key, String value) {
        SystemConfigEntity entity = new SystemConfigEntity();
        entity.setConfigKey(key.name().toLowerCase());
        entity.setConfigValue(value);
        int update = systemConfigDao.updateByKey(entity);
        if (update == 0) {
            return ResponseDTO.wrap(ResponseCodeConst.NOT_EXISTS);
        }
        //刷新缓存
        this.initSystemConfigCache();
        return ResponseDTO.succ();
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

    /**
     * 根据分组名称 获取获取系统设置
     *
     * @param group
     * @return
     */
    public List<SystemConfigDTO> getListByGroup(SystemConfigEnum.Group group) {
        List<SystemConfigEntity> entityList = systemConfigDao.getListByGroup(group.name());
        if (CollectionUtils.isEmpty(entityList)) {
            return Lists.newArrayList();
        }
        List<SystemConfigDTO> systemConfigList = SmartBeanUtil.copyList(entityList, SystemConfigDTO.class);
        return systemConfigList;
    }

}

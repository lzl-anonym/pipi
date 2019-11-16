package com.anonym.module.admin.employee.login;

import com.anonym.module.admin.employee.basic.EmployeeDao;
import com.anonym.module.admin.employee.basic.domain.dto.EmployeeDTO;
import com.anonym.module.admin.employee.login.domain.LoginCacheDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


@Service
public class EmployeeLoginCacheService {

    @Value("${login.expire.interval}")
    private Long expireInterval;

    /**
     * 存在登录状态的员工信息
     */
    private ConcurrentMap<Long, LoginCacheDTO> loginCache = Maps.newConcurrentMap();

    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 清理缓存
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void clearLogin() {
        List<Long> removeKey = Lists.newArrayList();
        for (Map.Entry<Long, LoginCacheDTO> entry : loginCache.entrySet()) {
            Long key = entry.getKey();
            LoginCacheDTO expressCache = entry.getValue();
            long expireTime = expressCache.getExpireTime();
            if (System.currentTimeMillis() > expireTime) {
                removeKey.add(key);
            }
        }
        if (CollectionUtils.isEmpty(removeKey)) {
            return;
        }
        removeKey.forEach(e -> loginCache.remove(e));
    }

    public void addLoginCache(EmployeeDTO employeeDTO) {
        Long expireTime = System.currentTimeMillis() + expireInterval;
        LoginCacheDTO loginCacheDTO = new LoginCacheDTO();
        loginCacheDTO.setEmployeeDTO(employeeDTO);
        loginCacheDTO.setExpireTime(expireTime);
        loginCache.put(employeeDTO.getId(), loginCacheDTO);
    }

    public void addLoginCache(Long employeeId) {
        EmployeeDTO employeeDTO = employeeDao.getEmployeeById(employeeId);
        if (employeeDTO != null) {
            Long expireTime = System.currentTimeMillis() + expireInterval;
            LoginCacheDTO loginCacheDTO = new LoginCacheDTO();
            loginCacheDTO.setEmployeeDTO(employeeDTO);
            loginCacheDTO.setExpireTime(expireTime);
            loginCache.put(employeeId, loginCacheDTO);
        }
    }

    public void updateCacheEmployee(EmployeeDTO employeeDTO) {
        LoginCacheDTO loginCacheDTO = loginCache.get(employeeDTO.getId());
        if (loginCacheDTO == null) {
            return;
        }
        loginCacheDTO.setEmployeeDTO(employeeDTO);
        loginCache.put(employeeDTO.getId(), loginCacheDTO);
    }

    public void updateCacheEmployee(Long employeeId) {
        LoginCacheDTO loginCacheDTO = loginCache.get(employeeId);
        if (loginCacheDTO == null) {
            return;
        }
        EmployeeDTO employeeDTO = employeeDao.getEmployeeById(employeeId);
        if (employeeDTO != null) {
            loginCacheDTO.setEmployeeDTO(employeeDTO);
            loginCache.put(employeeDTO.getId(), loginCacheDTO);
        }
    }

    public void updateExpireTime(Long employeeId) {
        LoginCacheDTO loginCacheDTO = loginCache.get(employeeId);
        if (loginCacheDTO == null) {
            return;
        }
        Long expireTime = System.currentTimeMillis() + expireInterval;
        loginCacheDTO.setExpireTime(expireTime);
        loginCache.put(employeeId, loginCacheDTO);
    }

    public void removeLoginCache(Long employeeId) {
        loginCache.remove(employeeId);
    }

    public void batchUpdateCacheEmployee(List<Long> employeeIds) {
        if (CollectionUtils.isEmpty(employeeIds)) {
            return;
        }
        Set<Long> cacheEmployeeIds = loginCache.keySet();

        List<Long> waitUpdateIds = employeeIds.stream().filter(t -> cacheEmployeeIds.contains(t)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(waitUpdateIds)) {
            return;
        }
        List<EmployeeDTO> employeeDTOList = employeeDao.getEmployeeByIds(waitUpdateIds);
        if (CollectionUtils.isEmpty(employeeDTOList)) {
            return;
        }
        employeeDTOList.forEach(e -> {
            LoginCacheDTO loginCacheDTO = loginCache.get(e.getId());
            loginCacheDTO.setEmployeeDTO(e);
            loginCache.put(e.getId(), loginCacheDTO);
        });
    }

    public LoginCacheDTO getLoginCacheDTO(Long employeeId) {
        return loginCache.get(employeeId);
    }
}

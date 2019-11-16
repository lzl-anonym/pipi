package com.anonym.module.admin.datascope.service;

import com.anonym.common.anno.AdminDataScope;
import com.anonym.module.admin.datascope.constant.DataScopeWhereInTypeEnum;
import com.anonym.module.admin.datascope.domain.dto.DataScopeSqlConfigDTO;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.utils.SmartRequestTokenUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class DataScopeSqlConfigService {

    private ConcurrentHashMap<String, DataScopeSqlConfigDTO> dataScopeMethodMap = new ConcurrentHashMap<>();

    @Autowired
    private DataScopeViewService dataScopeViewService;

    @Value("${swagger.packAge}")
    private String scanPackage;

    /**
     * 注解joinsql 参数
     */
    private static final String EMPLOYEE_PARAM = "#employeeIds";

    private static final String DEPARTMENT_PARAM = "#departmentIds";

    @PostConstruct
    private void initDataScopeMethodMap() {
        this.refreshDataScopeMethodMap();
    }

    /**
     * 刷新 所有添加数据范围注解的接口方法配置<class.method,DataScopeSqlConfigDTO></>
     *
     * @return
     */
    private Map<String, DataScopeSqlConfigDTO> refreshDataScopeMethodMap() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(scanPackage)).setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(AdminDataScope.class);
        for (Method method : methods) {
            AdminDataScope dataScopeAnnotation = method.getAnnotation(AdminDataScope.class);
            if (dataScopeAnnotation != null) {
                DataScopeSqlConfigDTO configDTO = new DataScopeSqlConfigDTO();
                configDTO.setDataScopeType(dataScopeAnnotation.dataScopeType().getType());
                configDTO.setJoinSql(dataScopeAnnotation.joinSql());
                configDTO.setWhereIndex(dataScopeAnnotation.whereIndex());
                dataScopeMethodMap.put(method.getDeclaringClass().getSimpleName() + "." + method.getName(), configDTO);
            }
        }
        return dataScopeMethodMap;
    }

    /**
     * 根据调用的方法获取，此方法的配置信息
     *
     * @param method
     * @return
     */
    public DataScopeSqlConfigDTO getSqlConfig(String method) {
        DataScopeSqlConfigDTO sqlConfigDTO = this.dataScopeMethodMap.get(method);
        return sqlConfigDTO;
    }

    /**
     * 组装需要拼接的sql
     *
     * @param sqlConfigDTO
     * @return
     */
    public String getJoinSql(DataScopeSqlConfigDTO sqlConfigDTO) {
        Integer dataScopeType = sqlConfigDTO.getDataScopeType();
        String joinSql = sqlConfigDTO.getJoinSql();
        LoginTokenDTO requestToken = SmartRequestTokenUtil.getRequestUser();
        Long employeeId = requestToken.getId();
        if (DataScopeWhereInTypeEnum.EMPLOYEE.getType().equals(sqlConfigDTO.getDataScopeWhereInType())) {
            List<Long> canViewEmployeeIds = dataScopeViewService.getCanViewEmployeeId(dataScopeType, employeeId);
            if (CollectionUtils.isEmpty(canViewEmployeeIds)) {
                return "";
            }
            String employeeIds = StringUtils.join(canViewEmployeeIds, ",");
            String sql = joinSql.replaceAll(EMPLOYEE_PARAM, employeeIds);
            return sql;
        }
        if (DataScopeWhereInTypeEnum.DEPARTMENT.getType().equals(sqlConfigDTO.getDataScopeWhereInType())) {
            List<Long> canViewDepartmentIds = dataScopeViewService.getCanViewDepartmentId(dataScopeType, employeeId);
            if (CollectionUtils.isEmpty(canViewDepartmentIds)) {
                return "";
            }
            String departmentIds = StringUtils.join(canViewDepartmentIds, ",");
            String sql = joinSql.replaceAll(DEPARTMENT_PARAM, departmentIds);
            return sql;
        }
        return "";
    }
}

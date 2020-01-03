package com.anonym.module.admin.privilege.service;

import com.anonym.constant.CommonConst;
import com.anonym.module.admin.privilege.domain.dto.PrivilegeRequestUrlVO;
import com.anonym.utils.SmartStringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


@Slf4j
@Service
public class PrivilegeRequestUrlService {

    /**
     * 系统所有requestUrl
     */
    private CopyOnWriteArrayList<PrivilegeRequestUrlVO> privilegeUrlDTOList = Lists.newCopyOnWriteArrayList();

    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public void initAllAdminUrl() {
        this.privilegeUrlDTOList.clear();

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.forEach((info, handlerMethod) -> {
            RestController restAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod().getDeclaringClass(), RestController.class);
            if (restAnnotation == null) {
                ResponseBody responseBody = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
                if (responseBody == null) {
                    return;
                }
            }
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            patterns = patterns.stream().filter(e -> e.contains(CommonConst.API_PREFIX_ADMIN)).collect(Collectors.toSet());

            if (CollectionUtils.isEmpty(patterns)) {
                return;
            }
            String className = (String) handlerMethod.getBean();
            String methodName = handlerMethod.getMethod().getName();
            List<String> list = SmartStringUtil.splitConvertToList(className, "\\.");
            String controllerName = list.get(list.size() - 1);
            String name = controllerName + "." + methodName;

            ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
            String methodComment;
            if (apiOperation != null) {
                methodComment = apiOperation.value();
            } else {
                ApiModelProperty apiModelProperty = handlerMethod.getMethod().getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null) {
                    methodComment = apiModelProperty.value();
                } else {
                    methodComment = handlerMethod.getMethod().getName();
                }
            }
            Set<String> urlSet = this.getUrlSet(patterns);
            for (String url : urlSet) {
                PrivilegeRequestUrlVO privilegeUrlDTO = new PrivilegeRequestUrlVO();
                privilegeUrlDTO.setUrl(url);
                privilegeUrlDTO.setName(name);
                privilegeUrlDTO.setComment(methodComment);
                this.privilegeUrlDTOList.add(privilegeUrlDTO);
            }
        });
    }

    private Set<String> getUrlSet(Set<String> patterns) {
        Set<String> urlSet = Sets.newHashSet();
        for (String url : patterns) {
            for (String ignoreUrl : CommonConst.CommonCollection.IGNORE_URL_MAPPING) {
                if (url.startsWith(ignoreUrl)) {
                    urlSet.add(url.substring(ignoreUrl.length() - 1));
                } else {
                    urlSet.add(url);
                }
            }
        }
        return urlSet;
    }

    public List<PrivilegeRequestUrlVO> getPrivilegeList() {
        return this.privilegeUrlDTOList;
    }

}

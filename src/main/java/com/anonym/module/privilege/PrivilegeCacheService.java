package com.anonym.module.privilege;

import com.anonym.common.constant.CommonConst;
import com.anonym.module.privilege.domain.PrivilegeUrlDTO;
import com.anonym.utils.SmartStringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
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

/**
 * [ 初始化 分离前后台权限URL ]
 */
@Service
public class PrivilegeCacheService {

    private CopyOnWriteArrayList<PrivilegeUrlDTO> backPrivilegeUrlDTOList = Lists.newCopyOnWriteArrayList();

    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public void initAllUrl() {
        this.backPrivilegeUrlDTOList.clear();

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.forEach((info, handlerMethod) -> {
            //只对Rest 服务进行权限验证
            RestController restAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod().getDeclaringClass(), RestController.class);
            if (restAnnotation == null) {
                ResponseBody responseBody = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
                if (responseBody == null) {
                    return;
                }
            }
            //employee 端只拿后台接口和未加相应注解的接口

            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            if (CollectionUtils.isEmpty(patterns)) {
                return;
            }
            Set<String> urlSet = this.getUrlSet(patterns);
            if (urlSet.size() > 1) {
                throw new ExceptionInInitializerError(String.format("other api not equal pc api which api is : %s", patterns.toString()));
            }
            String url = urlSet.iterator().next();

            String className = (String) handlerMethod.getBean();
            String methodName = handlerMethod.getMethod().getName();
            List<String> list = SmartStringUtil.splitConvertToList(className, "\\.");
            String controllerName = list.get(list.size() - 1);
            String name = controllerName + "." + methodName;

            ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
            String methodComment = null;
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

            PrivilegeUrlDTO privilegeUrlDTO = new PrivilegeUrlDTO();
            privilegeUrlDTO.setUrl(url);
            privilegeUrlDTO.setName(name);
            privilegeUrlDTO.setComment(methodComment);

            this.backPrivilegeUrlDTOList.add(privilegeUrlDTO);
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

    public List<PrivilegeUrlDTO> getBackPrivilegeList() {
        return this.backPrivilegeUrlDTOList;
    }

}

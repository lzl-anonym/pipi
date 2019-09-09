package com.anonym.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.anonym.common.anno.NoNeedLogin;
import com.anonym.common.anno.NoValidPrivilege;
import com.anonym.common.constant.CommonConst;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.common.token.IRequestToken;
import com.anonym.common.token.RequestTokenService;
import com.anonym.common.token.TokenCommonResponseCodeConst;
import com.anonym.module.privilege.EmployeePrivilegeCacheService;
import com.anonym.utils.RequestTokenUtil;
import com.anonym.utils.SmartStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 登录拦截器
 */
@Component
public class PrivilegeInterceptor extends HandlerInterceptorAdapter {

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    @Autowired
    private RequestTokenService requestTokenService;

    @Autowired
    private EmployeePrivilegeCacheService employeePrivilegeCacheService;

    private static Integer SUM = 0;

    /**
     * 拦截服务器端响应处理ajax请求返回结果
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With, Content-Type, " + "Accept, x-access-token");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires ", "-1");
        if (handler instanceof HandlerMethod) {
            //不需要登录的注解
            Boolean isNoNeedLogin = ((HandlerMethod) handler).getMethodAnnotation(NoNeedLogin.class) != null;
            if (isNoNeedLogin) {
                return true;
            }

            //放行的Uri前缀
            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            String target = uri.replaceFirst(contextPath, "");
            if (CommonConst.CommonCollection.contain(CommonConst.CommonCollection.IGNORE_URL, target)) {
                return true;
            }

            //需要做token校验, 消息头的token优先于请求query参数的token
            String xHeaderToken = request.getHeader("x-access-token");
            String xRequestToken = request.getParameter("x-access-token");
            String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
            if (null == xAccessToken) {
                this.outputResult(response, TokenCommonResponseCodeConst.LOGIN_ERROR);
                return false;
            }

            //根据token获取登录用户
            IRequestToken requestToken = requestTokenService.getEmployeeTokenInfo(xAccessToken);
            if (null == requestToken) {
                this.outputResult(response, TokenCommonResponseCodeConst.LOGIN_ERROR);
                return false;
            }

            //判断接口权限
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            String className = ((HandlerMethod) handler).getBeanType().getName();
            List<String> list = SmartStringUtil.splitConvertToList(className, "\\.");
            String controllerName = list.get(list.size() - 1);
            Method m = ((HandlerMethod) handler).getMethod();
            Class<?> cls = ((HandlerMethod) handler).getBeanType();
            boolean isClzAnnotation = cls.isAnnotationPresent(NoValidPrivilege.class);
            boolean isMethodAnnotation = m.isAnnotationPresent(NoValidPrivilege.class);
            NoValidPrivilege noValidPrivilege = null;
            if (isClzAnnotation) {
                noValidPrivilege = cls.getAnnotation(NoValidPrivilege.class);
            } else if (isMethodAnnotation) {
                noValidPrivilege = m.getAnnotation(NoValidPrivilege.class);
            }
            //不需验证权限
            if (noValidPrivilege != null) {
                RequestTokenUtil.setUser(request, requestToken);
                return true;
            }
            //需要验证权限
            Boolean privilegeValidPass = employeePrivilegeCacheService.checkEmployeeHavePrivilege(requestToken.getId(), controllerName, methodName);
            if (!privilegeValidPass) {
                this.outputResult(response, TokenCommonResponseCodeConst.NOT_HAVE_PRIVILEGES);
                return false;
            } else {
                RequestTokenUtil.setUser(request, requestToken);
            }
            return true;
        }
        return true;
    }

    private void outputResult(HttpServletResponse response, TokenCommonResponseCodeConst responseCodeConst) throws IOException {
        ResponseDTO<Object> wrap = ResponseDTO.wrap(responseCodeConst);
        String msg = JSONObject.toJSONString(wrap);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(msg);
        response.flushBuffer();
    }
}

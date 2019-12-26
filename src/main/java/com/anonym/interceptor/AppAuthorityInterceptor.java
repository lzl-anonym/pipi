package com.anonym.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.user.UserResponseCodeConst;
import com.anonym.module.user.basic.domain.UserDTO;
import com.anonym.module.user.login.UserLoginService;
import com.anonym.utils.SmartRequestTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * app 登录拦截器
 */
@Component
public class AppAuthorityInterceptor extends HandlerInterceptorAdapter {

    private static final String TOKEN_NAME = "x-access-token";

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 拦截服务器端响应处理ajax请求返回结果
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.crossDomainConfig(response);
        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String target = uri.replaceFirst(contextPath, "");
        if (CommonConst.CommonCollection.contain(CommonConst.CommonCollection.IGNORE_URL, target)) {
            return true;
        }

        String xHeaderToken = request.getHeader(TOKEN_NAME);
        String xRequestToken = request.getParameter(TOKEN_NAME);
        String xAccessToken = null != xHeaderToken ? xHeaderToken : xRequestToken;
        UserDTO userTokenInfo = null;
        if (null != xAccessToken) {
            userTokenInfo = userLoginService.getUserTokenInfo(xAccessToken);
            this.setRequestUser(userTokenInfo, request);
        }

        AppAuthorityLevel appAuthorityLevel = ((HandlerMethod) handler).getMethodAnnotation(AppAuthorityLevel.class);
        if (null != appAuthorityLevel && appAuthorityLevel.level() == AppAuthorityLevel.LOW) {
            return true;
        }

        if (null == xAccessToken || null == userTokenInfo) {
            this.outputResult(response, UserResponseCodeConst.LOGIN_ERROR);
            return false;
        }

        return true;
    }

    private void setRequestUser(UserDTO userTokenInfo, HttpServletRequest request) {
        if (null == userTokenInfo) {
            return;
        }
        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        tokenDTO.setId(userTokenInfo.getUserId());
        tokenDTO.setName(userTokenInfo.getNickname());
        SmartRequestTokenUtil.setUser(request, tokenDTO);
    }

    private void crossDomainConfig(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With, Content-Type, " + "Accept, x-access-token");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires ", "-1");
    }

    private void outputResult(HttpServletResponse response, UserResponseCodeConst responseCodeConst) throws IOException {
        ResponseDTO<Object> wrap = ResponseDTO.wrap(responseCodeConst);
        String msg = JSONObject.toJSONString(wrap);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(msg);
        response.flushBuffer();
    }
}

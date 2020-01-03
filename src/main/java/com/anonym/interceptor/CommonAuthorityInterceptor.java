package com.anonym.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.anonym.common.anno.AdminAuthorityLevel;
import com.anonym.common.anno.AppAuthorityLevel;
import com.anonym.common.domain.ResponseDTO;
import com.anonym.constant.CommonConst;
import com.anonym.module.admin.employee.login.EmployeeLoginTokenService;
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


@Component
public class CommonAuthorityInterceptor extends HandlerInterceptorAdapter {

    private static final String TOKEN_NAME = "x-access-token";

    @Value("${access-control-allow-origin}")
    private String accessControlAllowOrigin;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private EmployeeLoginTokenService employeeLoginTokenService;

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
        //跨域设置
        this.crossDomainConfig(response);

        boolean isHandler = handler instanceof HandlerMethod;
        if (!isHandler) {
            return true;
        }

        AppAuthorityLevel appAuthorityLevel = ((HandlerMethod) handler).getMethodAnnotation(AppAuthorityLevel.class);
        if (null != appAuthorityLevel && appAuthorityLevel.level() == AppAuthorityLevel.LOW) {
            return true;
        }

        AdminAuthorityLevel adminAuthorityLevel = ((HandlerMethod) handler).getMethodAnnotation(AdminAuthorityLevel.class);
        if (null != adminAuthorityLevel && adminAuthorityLevel.level() == AdminAuthorityLevel.LOW) {
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
        if (null == xAccessToken) {
            this.outputResult(response, UserResponseCodeConst.LOGIN_ERROR);
            return false;
        }

        UserDTO userToken = userLoginService.getUserTokenInfo(xAccessToken);
        LoginTokenDTO employeeToken = employeeLoginTokenService.getEmployeeTokenInfo(xAccessToken);
        if (null == userToken && null == employeeToken) {
            this.outputResult(response, UserResponseCodeConst.LOGIN_ERROR);
            return false;
        }

        LoginTokenDTO tokenDTO = new LoginTokenDTO();
        if (null != userToken) {
            tokenDTO.setId(userToken.getUserId());
            tokenDTO.setName(userToken.getNickname());
        } else {
            tokenDTO.setId(employeeToken.getId());
            tokenDTO.setName(employeeToken.getName());
        }
        SmartRequestTokenUtil.setUser(request, tokenDTO);
        return true;
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

    /**
     * 错误输出
     *
     * @param response
     * @param responseCodeConst
     * @throws IOException
     */
    private void outputResult(HttpServletResponse response, UserResponseCodeConst responseCodeConst) throws IOException {
        ResponseDTO<Object> wrap = ResponseDTO.wrap(responseCodeConst);
        String msg = JSONObject.toJSONString(wrap);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(msg);
        response.flushBuffer();
    }
}

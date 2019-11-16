package com.anonym.utils;

import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


public class SmartRequestTokenUtil {

    private static final String USER_KEY = "dear_user";

    public static void setUser(HttpServletRequest request, LoginTokenDTO requestToken) {
        request.setAttribute(USER_KEY, requestToken);
    }

    public static LoginTokenDTO getRequestUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                return (LoginTokenDTO) request.getAttribute(USER_KEY);
            }
        }
        return null;
    }

    public static Long getRequestUserId() {
        LoginTokenDTO user = getRequestUser();
        if (null == user) {
            return null;
        }
        return user.getId();
    }

}

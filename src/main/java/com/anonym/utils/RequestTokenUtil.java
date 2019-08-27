package com.anonym.utils;

import com.anonym.common.token.IRequestToken;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author lizongliang
 */
public class RequestTokenUtil {

    private static final String USER_KEY = "smart_admin_user";

    private static ThreadLocal<IRequestToken> RequestUserThreadLocal = new ThreadLocal<IRequestToken>();

    public static void setUser(HttpServletRequest request, IRequestToken requestToken) {
        request.setAttribute(USER_KEY, requestToken);
        RequestUserThreadLocal.set(requestToken);
    }

    public static IRequestToken getUser() {
        return RequestUserThreadLocal.get();
    }

    public static IRequestToken getRequestUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                return (IRequestToken) request.getAttribute(USER_KEY);
            }
        }
        return null;
    }

    public static String getRequestUri() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request != null) {
                String uri = request.getRequestURI();
                String contextPath = request.getContextPath();
                return uri.replaceFirst(contextPath, "");
            }
        }
        return null;
    }
}

package com.attendance.common.interceptor;

import com.attendance.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tomecs.
 */
public class SessionInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {String uri = request.getRequestURI();
        if((uri.indexOf("login")>=0)||(uri.indexOf("sign")>=0)||(uri.indexOf("error")>=0)){
            return true;
        }
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("userinfo");
        if(user!=null){
            return true;
        }
        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

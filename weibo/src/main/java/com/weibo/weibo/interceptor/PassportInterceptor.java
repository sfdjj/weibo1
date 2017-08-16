package com.weibo.weibo.interceptor;

import com.weibo.weibo.dao.LoginTicketDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.LoginTicket;
import com.weibo.weibo.model.User;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by jwc on 2017/7/22.
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;/*
        for(Cookie cookie:httpServletRequest.getCookies()) {
            if(cookie.getName().equals("ticket")) {
                ticket = cookie.getValue();
                break;y1111111
            }
        }*/
        ticket = WeiboUtil.getCookieTicket(httpServletRequest);
        if(ticket==null) {
            return true;
        }
        LoginTicket loginTicket = loginTicketDAO.selectTicketByTicket(ticket);
        if(loginTicket==null) {
            return true;
        }
        if(loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0) {
            return true;
        }
        User user = userDAO.selectById(loginTicket.getUserId());
        hostHolder.setUser(user);
        //User t = hostHolder.getUser();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null) {
            modelAndView.addObject("user",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}

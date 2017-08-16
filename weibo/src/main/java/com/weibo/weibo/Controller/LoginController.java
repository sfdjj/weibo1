package com.weibo.weibo.Controller;

import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.async.Producter;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.service.LoginTicketService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jwc on 2017/7/21.
 */
@Controller
public class LoginController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    LoginTicketService loginTicketService;

    @Autowired
    Producter producter;


    @RequestMapping(path={"/register"})
    @ResponseBody
    public String reg(@RequestParam("name") String username,
                      @RequestParam("email") String mail,
                      @RequestParam("password") String password,
                      HttpServletResponse httpServletResponse) {
        Map<String,Object> map = userService.reg(username,password,mail);
        try{
            if(map.containsKey("ticket")) { /*
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                httpServletResponse.addCookie(cookie);*/
                WeiboUtil.UpdateCookieTicket(httpServletResponse,map.get("ticket").toString(),
                        3600 * 24 * 10);
                map.put("msg","注册成功");
                return WeiboUtil.getJSONString(0,map);
            }else {
                return WeiboUtil.getJSONString(1,map);
            }
        }catch (Exception e) {
            return e.getMessage()+WeiboUtil.getJSONString(1,map);
        }
    }

    @RequestMapping(path = {"/loginPage"}, method = {RequestMethod.GET})
    public String loginPage() {
        if (hostHolder.getUser() != null) {
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping(path = {"/regPage"}, method = {RequestMethod.GET})
    public String regPage() {
        if (hostHolder.getUser() != null) {
            return "redirect:/index";
        }
        return "register";
    }

    @RequestMapping(path={"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("email") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse httpServletResponse) {
        Map<String,Object> map = userService.login(username,password);
        try{
            if(map.containsKey("ticket")) { /*
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                httpServletResponse.addCookie(cookie);*/
                WeiboUtil.UpdateCookieTicket(httpServletResponse,map.get("ticket").toString(),
                        3600 * 24 * 10);
                int userId = userService.getUserById(loginTicketService.getTicket(map.get("ticket").toString()).getUserId()).getId();
                producter.pushEvent(new EventModel().setEventType(EventType.LOGIN).setActorId(userId));

                return WeiboUtil.getJSONString(0,map);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("code",true);
//                return jsonObject.toString();
            }else {
                return WeiboUtil.getJSONString(1,map);
            }
        }catch (Exception e) {
            return e.getMessage()+WeiboUtil.getJSONString(1,map);
        }
    }

    @RequestMapping(path = {"/logout"})
    @LoginRequired(loginRequired = true)
    public String logout(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
        String ticket = null;/*
        for(Cookie cookie:httpServletRequest.getCookies()) {
            if(cookie.getName().equals("ticket")) {
                ticket = cookie.getValue();
                break;
            }
        }*/
        ticket = WeiboUtil.getCookieTicket(httpServletRequest);
        Map<String,Object> map = userService.logOut(ticket);
        if(map.containsKey("success")) {
            WeiboUtil.UpdateCookieTicket(httpServletResponse,null,0);
        }
        return "redirect:/index";
    }

}

package com.weibo.weibo.service;

import com.weibo.weibo.dao.LoginTicketDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.LoginTicket;
import com.weibo.weibo.model.User;
import com.weibo.weibo.util.WeiboUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jwc on 2017/7/22.
 */
@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public User getUserById(int userId) {
        return userDAO.selectById(userId);
    }

    public User getUserByName(String name) {
        return userDAO.selectByName(name);
    }


    public  Map<String,Object> login(String mail,String password) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(mail)) {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank("password")) {
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.selectByMail(mail);
        if(user==null) {
            map.put("msg","该用户不存在");
            return map;
        }

        if(!user.getPassword().equals(WeiboUtil.MD5(password+user.getSalt()))){
            map.put("msg","用户名或密码不正确");
            return map;
        }
        String ticket = getTicket(user.getId());
        map.put("ticket",ticket);
        map.put("msg","登陆成功");
        return map;
    }

    public Map<String,Object> reg(String username,String password,String mail) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)) {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("msg","密码不能为空");
            return map;
        }
        if(password.length()<8) {
            map.put("msg","密码长度不能低于8位");
            return map;
        }
        try{
            Integer.parseInt(password);
            map.put("msg","密码强度太低，请使用数字+字母的组合方式");
            return map;
        }catch (Exception e){}
        String postfix = mail.substring(mail.lastIndexOf("@")+1);
        if(!WeiboUtil.isMail(postfix)) {
            map.put("msg","请输入合法的邮箱");
            return map;
        }
        User user = userDAO.selectByName(username);
        if(user!=null) {
            map.put("msg","该用户已存在");
            return map;
        }
        user = userDAO.selectByMail(mail);
        if(user!=null) {
            map.put("msg","改邮箱已注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setMail(mail);
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setPassword(WeiboUtil.MD5(password+salt));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        userDAO.addUser(user);
        String ticket = getTicket(userDAO.selectByName(username).getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> logOut(String ticket) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(ticket)) {
            map.put("msg","你还没有登陆");
            return map;
        }
        loginTicketDAO.updateStatus(ticket,1);
        map.put("success","退出成功");
        return map;
    }

    public String getTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        String ticket = UUID.randomUUID().toString().replaceAll("-","");
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+3600*1000*24*5);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(ticket);
        loginTicketDAO.addLoginTicket(loginTicket);
        return ticket;

    }


}

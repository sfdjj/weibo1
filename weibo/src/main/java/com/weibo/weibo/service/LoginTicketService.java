package com.weibo.weibo.service;

import com.weibo.weibo.dao.LoginTicketDAO;
import com.weibo.weibo.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jwc on 2017/7/29.
 */
@Service
public class LoginTicketService {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    public LoginTicket getTicket(String ticket) {
        return loginTicketDAO.selectTicketByTicket(ticket);
    }

}

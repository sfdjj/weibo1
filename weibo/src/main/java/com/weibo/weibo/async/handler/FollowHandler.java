package com.weibo.weibo.async.handler;

import com.weibo.weibo.async.EventHandler;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.service.MessageService;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jwc on 2017/7/31.
 */
@Component
public class FollowHandler implements EventHandler{

    @Autowired
    UserDAO userDAO;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel eventModel) {
        int fromId = WeiboUtil.DEFAULT_SYSTEM_ID;
        int userId = eventModel.getEntityOwner();
        String content = userDAO.selectById(eventModel.getActorId()).getName()+"关注了你";
        messageService.addMessage(fromId,userId,content);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}

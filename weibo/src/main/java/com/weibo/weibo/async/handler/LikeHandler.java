package com.weibo.weibo.async.handler;

import com.weibo.weibo.async.EventHandler;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.dao.MicrobloggingDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.JedisAdapterService;
import com.weibo.weibo.service.MessageService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.JedisUtils;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwc on 2017/7/29.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MessageService messageService;

    @Autowired
    MicrobloggingDAO microbloggingDAO;

    @Autowired
    JedisAdapterService jedisAdapterService;

    @Override
    public void doHandle(EventModel eventModel) {
        if(eventModel.getEntityType()== WeiboUtil.ENTITY_TYPE_WEIBO) {
            doWeiboLike(eventModel);
        }
        if(eventModel.getEntityType()==WeiboUtil.ENTITY_TYPE_COMMENT) {
            doCommentLike(eventModel);
        }
    }

    public void doWeiboLike(EventModel eventModel) {
        User fromUser = userDAO.selectById(eventModel.getActorId());
        User toUser = userDAO.selectById(eventModel.getEntityOwner());
        String content = fromUser.getName()+"赞了你的微博1次";
        int fromId = WeiboUtil.DEFAULT_SYSTEM_ID;
        messageService.addMessage(fromId,toUser.getId(),content);
        String key = JedisUtils.getLikeKey(eventModel.getEntityType(),eventModel.getEntityId());
        int likeCount = (int)jedisAdapterService.scard(key);
        microbloggingDAO.updateLikeCount(likeCount,eventModel.getEntityId());
    }

    public void doCommentLike(EventModel eventModel) {
        User fromUser = userDAO.selectById(eventModel.getActorId());
        User toUser = userDAO.selectById(eventModel.getEntityOwner());
        String content = fromUser.getName()+"赞了你的评论1次";
        int fromId = WeiboUtil.DEFAULT_SYSTEM_ID;
        messageService.addMessage(fromId,toUser.getId(),content);
//        String key = JedisUtils.getLikeKey(eventModel.getEntityType(),eventModel.getEntityId());
//        int likeCount = (int)jedisAdapterService.scard(key);
//        microbloggingDAO.updateLikeCount(likeCount,eventModel.getEntityId());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        List<EventType> list = new ArrayList<>();
        list.add(EventType.LIKE);
        return list;
    }
}

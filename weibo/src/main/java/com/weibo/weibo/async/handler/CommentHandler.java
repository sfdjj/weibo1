package com.weibo.weibo.async.handler;

import com.weibo.weibo.async.EventHandler;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.dao.CommentDAO;
import com.weibo.weibo.dao.MicrobloggingDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.MessageService;
import com.weibo.weibo.util.JedisUtils;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jwc on 2017/7/31.
 */
@Component
public class CommentHandler implements EventHandler{

    @Autowired
    UserDAO userDAO;

    @Autowired
    MessageService messageService;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    MicrobloggingDAO microbloggingDAO;

    @Override
    public void doHandle(EventModel eventModel) {
        if(eventModel.getEntityType()== WeiboUtil.ENTITY_TYPE_WEIBO) {
            doWeiboComment(eventModel);
        }
        if(eventModel.getEntityType()==WeiboUtil.ENTITY_TYPE_COMMENT) {
            doCommentComment(eventModel);
        }
    }

    public void doWeiboComment(EventModel eventModel) {
        User fromUser = userDAO.selectById(eventModel.getActorId());
        User toUser = userDAO.selectById(eventModel.getEntityOwner());
        String content = fromUser.getName()+"评论了你的微博";
        int fromId = WeiboUtil.DEFAULT_SYSTEM_ID;
        messageService.addMessage(fromId,toUser.getId(),content);
        int commentCount = commentDAO.getCommentCount(eventModel.getEntityId(),eventModel.getEntityType());
        microbloggingDAO.updateCommentCount(commentCount,eventModel.getEntityId());
    }

    public void doCommentComment(EventModel eventModel) {
        User fromUser = userDAO.selectById(eventModel.getActorId());
        User toUser = userDAO.selectById(eventModel.getEntityOwner());
        String content = fromUser.getName()+"回复了你的评论";
        int fromId = WeiboUtil.DEFAULT_SYSTEM_ID;
        messageService.addMessage(fromId,toUser.getId(),content);
//        int commentCount = commentDAO.getCommentCount(eventModel.getEntityType(),eventModel.getEntityId());
//        microbloggingDAO.updateCommentCount(commentCount,eventModel.getEntityId());
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}

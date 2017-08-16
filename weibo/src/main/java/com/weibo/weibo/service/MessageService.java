package com.weibo.weibo.service;

import com.weibo.weibo.dao.MessageDAO;
import com.weibo.weibo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/26.
 */
@Service
public class MessageService {

    @Autowired
    MessageDAO messageDAO;

    public List<Message> getConversationList(int userId,int offset,int limit) {
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public List<Message> getMessage(String conversation,int offset,int limit,int userId) {
        return messageDAO.getConversationDetail(conversation,offset,limit,userId);
    }

    public int getConvesationUnreadCount(int userId,String conversationId) {
        return messageDAO.getConvesationUnreadCount(userId,conversationId);
    }

    public Map<String,Object> deleteMessage(int messageId,int userId) {
        Map<String,Object> map = new HashMap<>();
        Message message = messageDAO.selectMessageById(messageId);
        if(message.getFromId()==userId) {
            messageDAO.DeleteFromMessage(1,messageId);
            map.put("success","删除成功");
            return map;
        }
        if(message.getToId()==userId) {
            messageDAO.DeleteToMessage(1,messageId);
            map.put("success","删除成功");
            return map;
        }
        map.put("msg","删除失败");
        return map;
    }

    public void updateHasRead(int messageId) {
        messageDAO.updateHasRead(1,messageId);
    }

    public Map<String,Object> addMessage(int fromId,int toId,String content) {
        Map<String,Object> map = new HashMap<>();
        if(fromId==toId) {
            map.put("msg","接收用户不能为自己");
            return map;
        }
        Message message = new Message();
        message.setContent(content);
        message.setCreatedDate(new Date());
        message.setFromId(fromId);
        message.setToId(toId);
        message.setConversationId(String.format("%d_%d",Math.min(fromId,toId),Math.max(fromId,toId)));
        message.setHasRead(0);
        message.setFromDelete(0);
        message.setToDelete(0);
        messageDAO.addMessage(message);
        map.put("success","发送成功");
        return map;
    }

}

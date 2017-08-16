package com.weibo.weibo.async.handler;

import com.weibo.weibo.async.EventHandler;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.dao.MicrobloggingDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.Microblogging;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.JedisAdapterService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.JedisUtils;
import com.weibo.weibo.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jwc on 2017/7/28.
 */
@Component
public class LoginHandler implements EventHandler {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MailUtil mailUtil;

    @Autowired
    JedisAdapterService jedisAdapterService;

    @Autowired
    MicrobloggingDAO microbloggingDAO;

    @Override
    public void doHandle(EventModel eventModel) {
        try {
            User user = userDAO.selectById(eventModel.getActorId());
            String subject = "欢迎登陆";
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("subject",subject);
            mailUtil.sendWithHTMLTemplate(map);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    public void pull(int userId) {
//        String followKey = JedisUtils.getFollowKey(userId);
//        Set<String> set = jedisAdapterService.getZ(followKey,0,(int)jedisAdapterService.zcard(followKey));
//        for(String s:set) {
//            int fromId = Integer.parseInt(s);
//            String push = JedisUtils.getPushKey(fromId,userId);
//            if(jedisAdapterService.llen(push)<=0) {
//                List<Microblogging> list = microbloggingDAO.selectByUserIdAndOffset()
//                String pull = JedisUtils.getPullKey(userId,fromId);
//                String value = entityType+"_"+entityId;
//                jedisAdapterService.lpush(pull,value);
//            }
//        }
//
//    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}

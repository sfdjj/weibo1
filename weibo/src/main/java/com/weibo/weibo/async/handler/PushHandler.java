package com.weibo.weibo.async.handler;

import com.weibo.weibo.async.EventHandler;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.service.JedisAdapterService;
import com.weibo.weibo.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by jwc on 2017/8/3.
 */
@Component
public class PushHandler implements EventHandler {

    @Autowired
    JedisAdapterService jedisAdapterService;

    @Override
    public void doHandle(EventModel eventModel) {
        int userId = eventModel.getActorId();
        int entityType = eventModel.getEntityType();
        int entityId = eventModel.getEntityId();
        String followerKey = JedisUtils.getFollowerKey(userId);
        Set<String> list = jedisAdapterService.zrange(followerKey,0,(int)jedisAdapterService.zcard(followerKey));
        for(String s:list) {
            int toId = Integer.parseInt(s);
            String pushKey = JedisUtils.getPushKey(toId);
            String value = entityType+"_"+entityId;
            jedisAdapterService.lpush(pushKey,value);
        }


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.PUSH);
    }
}

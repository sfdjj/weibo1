package com.weibo.weibo.async;

import com.alibaba.fastjson.JSON;
import com.weibo.weibo.service.JedisAdapterService;
import com.weibo.weibo.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jwc on 2017/7/28.
 */
@Component
public class Producter {

    @Autowired
    JedisAdapterService jedisAdapterService;

    public boolean pushEvent(EventModel eventModel) {
        try {
            jedisAdapterService.lpush(JedisUtils.EventQueueKey, JSON.toJSONString(eventModel));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}

package com.weibo.weibo.async;

import com.weibo.weibo.service.JedisAdapterService;
import com.weibo.weibo.util.JedisUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/28.
 */
@Service
public class consumer implements InitializingBean,ApplicationContextAware{

    private ApplicationContext applicationContext;

    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    @Autowired
    JedisAdapterService jedisAdapterService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null) {
            for(Map.Entry<String,EventHandler> map : beans.entrySet()) {
                List<EventType> list = map.getValue().getSupportEventTypes();
                for(EventType eventType:list) {
                    if(config.containsKey(eventType)) {
                        config.get(eventType).add(map.getValue());
                    }else {
                        config.put(eventType,new ArrayList<EventHandler>());
                        config.get(eventType).add(map.getValue());
                        //System.out.println(map.getValue());
                    }
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    List<String> list = jedisAdapterService.brpop(0, JedisUtils.EventQueueKey);
                    for(String s:list) {
                        if(s.equals(JedisUtils.EventQueueKey)) {
                            continue;
                        }
                        //System.out.println(s);
                        EventModel eventModel = jedisAdapterService.getObject(s,EventModel.class);
                        //System.out.println(eventModel.getEventType());
                        if(eventModel==null) {
                            continue;
                        }
                        if(!config.containsKey(eventModel.getEventType())) {
                            System.out.println("没有找到对应对象");
                            continue;
                        }
                        List<EventHandler> l = config.get(eventModel.getEventType());
                        for(EventHandler eventHandler:l) {
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

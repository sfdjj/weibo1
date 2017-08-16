package com.weibo.weibo.async;

import java.util.List;

/**
 * Created by jwc on 2017/7/28.
 */
public interface EventHandler {
    void doHandle(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}

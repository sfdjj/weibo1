package com.weibo.weibo.model;

import org.springframework.stereotype.Component;

/**
 * Created by jwc on 2017/7/22.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();
    public void setUser(User user) {
        users.set(user);
    }
    public User getUser() {
        return users.get();
    }
    public void clear() {
        users.remove();
    }
}

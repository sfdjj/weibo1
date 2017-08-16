package com.weibo.weibo.async;

/**
 * Created by jwc on 2017/7/28.
 */
public enum EventType {
    LOGIN(0),
    LIKE(1),
    COMMENT(2),
    FOLLOW(3),
    PUSH(4);

    private int value;
    EventType(int value) {
        this.value = value;
    }
    public int getValue() {
        return getValue();
    }
}

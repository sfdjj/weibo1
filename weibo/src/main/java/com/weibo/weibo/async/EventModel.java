package com.weibo.weibo.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jwc on 2017/7/28.
 */
public class EventModel {
    private EventType eventType;
    private int actorId;
    private int entityId;
    private int entityType;
    private int entityOwner;
    private Map<String,String> map = new HashMap<>();

    public EventModel() {

    }

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwner() {
        return entityOwner;
    }

    public EventModel setEntityOwner(int entityOwner) {
        this.entityOwner = entityOwner;
        return this;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}

package com.weibo.weibo.service;

import com.weibo.weibo.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jwc on 2017/7/29.
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapterService jedisAdapterService;

    public Map<String,Object> like(int entityType,int entityId,int userId) {
        Map<String,Object> map = new HashMap<>();
        String likeKey = JedisUtils.getLikeKey(entityType,entityId);
        String value = userId+"";
        if(jedisAdapterService.sismember(likeKey,value)) {
            map.put("msg","你已经点过赞");
            return map;
        }
        jedisAdapterService.sadd(likeKey,value);
        String dislikeKey = JedisUtils.getDislikeKey(entityType,entityId);
        jedisAdapterService.srem(dislikeKey,value);
        map.put("success","点赞成功");
        return map;
    }

    public Map<String,Object> dislike(int entityType,int entityId,int userId) {
        Map<String,Object> map = new HashMap<>();
        String dislikeKey = JedisUtils.getDislikeKey(entityType,entityId);
        String likeKey = JedisUtils.getLikeKey(entityType,entityId);
        String value = userId+"";
        if(!jedisAdapterService.sismember(likeKey,value)) {
            map.put("msg","你还没有点赞");
            return map;
        }
        jedisAdapterService.srem(likeKey,value);
        if(jedisAdapterService.sismember(dislikeKey,value)) {
            map.put("msg","你已经踩过了");
            return map;
        }
        jedisAdapterService.sadd(dislikeKey,value);
        map.put("success","取消成功");
        return map;
    }

}

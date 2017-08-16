package com.weibo.weibo.service;

import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.User;
import com.weibo.weibo.util.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Transaction;

import java.util.*;

/**
 * Created by jwc on 2017/7/31.
 */
@Service
public class FollowService {

    @Autowired
    JedisAdapterService jedisAdapterService;

    @Autowired
    UserDAO userDAO;

    public Map<String,Object> follow(int userId,int followUserId) {
        Map<String,Object> map = new HashMap<>();
        if(userId==followUserId) {
            map.put("msg","你不能关注自己");
            return map;
        }
        String followKey = JedisUtils.getFollowKey(userId);
        String followerKey = JedisUtils.getFollowerKey(followUserId);
        if(jedisAdapterService.zRank(followKey,followUserId+"")) {
            map.put("msg","你已经关注过他");
            return map;
        }
        jedisAdapterService.zSet(followKey,followUserId+"");
        jedisAdapterService.zSet(followerKey,userId+"");
        map.put("success","关注成功");
        return map;
    }

    public Map<String,Object> remFollow(int userId,int followUserId) {
        Map<String,Object> map = new HashMap<>();
        String followKey = JedisUtils.getFollowKey(userId);
        String followerKey = JedisUtils.getFollowerKey(followUserId);
        jedisAdapterService.remZset(followKey,followUserId+"");
        jedisAdapterService.remZset(followerKey,userId+"");
        map.put("success","取消关注成功");
        return map;
    }

    public int getFolloweeCount(String key) {
        return (int)jedisAdapterService.zcard(key);
    }


    public  List<Integer> getFollowList(String key, int offset, int limit) {
        List<Integer> list = new ArrayList<>();
        Set<String> set = jedisAdapterService.getZ(key,offset,limit);
        for(String l:set) {
            list.add(Integer.parseInt(l));
        }
        return list;
    }


    public boolean isFollower(int userId,String key,int anotherId) {
        if(jedisAdapterService.zRank(key,userId+"")) {
            return true;
        }
        return false;
    }

}

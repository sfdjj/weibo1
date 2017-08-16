package com.weibo.weibo.util;

/**
 * Created by jwc on 2017/7/27.
 */
public class JedisUtils {

    public static String EventQueueKey = "EVENT";
    public static String LIKE_KEY = "BIZ_LIKE";
    public static String DISLIKE_KEY = "BIZ_DISLIKE";

    public static String getPushKey(int toId) {
        String s = "PUSH"+toId;
        return s;
    }

    public static String getPullKey(int fromId,int toId) {
        String s = fromId+"PULL"+toId;
        return s;
    }

    public static String getLikeKey(int entityType,int entityId) {
        String s = "LIKE"+":"+entityType+":"+entityId;
        return s;
    }

    public static String getDislikeKey(int entityType,int entityId) {
        String s = "DISLIKE"+":"+entityType+":"+entityId;
        return s;
    }

    public static String getFollowKey(int id){
        String s = "FOLLOW"+":"+id;
        return s;
    }

    public static String getFollowerKey(int id) {
        String s = "FOLLOWER"+":"+id;
        return s;
    }

    /*
    public static void main(String args[]) {
        JedisAdapterService jedisAdapterService = new JedisAdapterService();
        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.LOGIN);
        String s = JSONObject.toJSONString(eventModel);
        System.out.println(s);
        EventModel eventModel1 = JSONObject.parseObject(s,EventModel.class);
        EventModel eventModel2 = jedisAdapterService.getObject(s,EventModel.class);
        System.out.println(eventModel1.getEventType());

    }
    */
/*
    public static void main(String args[]) {
        JedisPool jedisPool = new JedisPool();
        Jedis jedis  = jedisPool.getResource();
        jedis.lpush("EVENT","132efwe");
    }
*/
/*

    public static void main(String args[]) {
        Jedis jedis = new Jedis();
        jedis.flushAll();
        jedis.set("set1","111");
        jedis.set("set2","222");
        jedis.incr("set1");
        jedis.incrBy("set2",322);
        jedis.setex("set1",5,"111");
        System.out.println(jedis.get("set1"));
        System.out.println(jedis.get("set2"));
        System.out.println(jedis.keys("*"));
        jedis.lpush("l9","1","2","3");
        for(int i=0;i<10;i++) {
            jedis.lpush("l9",i+"");
        }
        System.out.println(jedis.lrange("l9",0,15));
        System.out.println(jedis.llen("l9"));
        System.out.println(jedis.lrange("l9",0,15));
        System.out.println(jedis.lpop("l9"));
        System.out.println(jedis.lrange("l9",0,15));
        System.out.println(jedis.rpop("l9"));
        System.out.println(jedis.lrange("l9",0,15));
        System.out.println(jedis.lrem("l9",1,"0"));
        System.out.println(jedis.lrange("l9",0,15));
        jedis.linsert("l9", BinaryClient.LIST_POSITION.BEFORE,"5","123");
        System.out.println(jedis.lrange("l9",0,15));
        System.out.println(jedis.lindex("l9",4));
        jedis.lrem("l9",2,"2");
        System.out.println(jedis.lrange("l9",0,15));
        jedis.lset("l9",1,"102");
        System.out.println(jedis.lrange("l9",0,15));

        jedis.sadd("ss1","1","2","3");
        for(int i=0;i<10;i++) {
            jedis.sadd("ss1",""+i);
            jedis.sadd("ss2",i+i+"");
        }
        System.out.println(jedis.smembers("ss1"));
        System.out.println(jedis.smembers("ss2"));
        System.out.println(jedis.sinter("ss1","ss2"));
        System.out.println(jedis.sinterstore("ss3","ss1","ss2"));
        System.out.println(jedis.smembers("ss3"));
        System.out.println(jedis.sunion("ss1","ss2"));
        System.out.println(jedis.sunionstore("ss4","ss1","ss2"));
        System.out.println(jedis.smembers("ss4"));
        System.out.println(jedis.smove("ss1","ss2","2"));
        System.out.println(jedis.smembers("ss1"));
        System.out.println(jedis.smembers("ss2"));
        System.out.println(jedis.sdiff("ss1","ss2"));

        jedis.hset("h1","1","wer");
        jedis.hset("h1","2","ewff");
        jedis.hset("h1","3","efwe");
        //System.out.println(jedis.hincrBy("h1","1",11));
        System.out.println(jedis.hget("h1","1"));
        System.out.println(jedis.hgetAll("h1"));
        System.out.println(jedis.hdel("h1","1"));
        System.out.println(jedis.hget("h1","1"));
        System.out.println(jedis.hincrBy("h1","1",11));
        System.out.println(jedis.hget("h1","1"));

    }
*/
}

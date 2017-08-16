package com.weibo.weibo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jwc on 2017/7/28.
 */
@Component
public class JedisAdapterService implements InitializingBean {
    private JedisPool jedisPool = null;
    private Jedis jedis = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = new JedisPool("127.0.0.1",6379);
    }


    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void set(String key,String value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.set(key,value);
        }catch (Exception e) {
            System.out.println(e);
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public void zSet(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zadd(key,new Date().getTime(),value);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrange(String key,int offset,int limit) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrange(key,offset,limit);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long remZset(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrem(key,value);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.llen(key);
        }catch (Exception e) {
            return -1;
        }
        finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        }catch (Exception e) {
            return -1;
        }
        finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public Set<String> getZ(String key, int offset, int limit) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //return jedis.zrangeByScore(key,"0",new Date().toString(),offset,limit);
            //return jedis.zrangeByScore(key,0,new Date().toString(),offset,limit);
            return jedis.zrange(key,offset,limit);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpop(key);
        }catch (Exception e) {
            System.out.println();
            return null;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key,String value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean zRank(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if(jedis.zrank(key,value)!=null) {
                return true;
            }
            return false;
        }catch (Exception e) {
            System.out.println();
            return false;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long srem(String key,String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key,member);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public long lpush(String key,String value) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.lpush(key,value);
            return 1;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
        finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        List<String> list = new ArrayList<>();
        try {
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout,key);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return list;
        }
        finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object object) {
        set(key, JSON.toJSONString(object));
    }
    public <T> T getObject(String key,Class<T> clazz ) {
        if(key!=null) {
            return JSONObject.parseObject(key,clazz);
        }
        return null;
    }

}

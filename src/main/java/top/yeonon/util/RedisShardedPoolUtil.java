package top.yeonon.util;

import edu.princeton.cs.algs4.SET;
import top.yeonon.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class RedisShardedPoolUtil {
    /**
     * 重新设置过期时间，时间单位是秒
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key {} error ", key, e);
            return null;
        }
        return result;
    }

    /**
     *设置带过期时间的key,时间单位是秒
     */
    public static String setex(String key, String value, int exTime) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String set(String key, String value) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String getSet(String key, String value) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getset key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static Long setnx(String key, String value) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key {}, value {} error ", key, value, e);
            return null;
        }
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static String hmset(String key, Map<String, String> hash) {
        ShardedJedis jedis;
        String result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.hmset(key, hash);
        } catch (Exception e) {
            log.error("hmset key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Long append(String key, String value) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.append(key, value);
        } catch (Exception e) {
            log.error("append key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Long sadd(String key, String ...members) {
        ShardedJedis jedis;
        Long result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.sadd(key, members);
        } catch (Exception e) {
            log.error("sadd key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static Boolean exists(String key) {
        ShardedJedis jedis;
        Boolean result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.exists(key);
        } catch (Exception e) {
            log.error("exists key {}, error ", key, e);
            return null;
        }
        return result;
    }

    public static List<String> hmget(String key, String ...fields) {
        ShardedJedis jedis;
        List<String> result;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.hmget(key, fields);
        } catch (Exception e) {
            log.error("exists key {}, error ", key, e);
            return null;
        }
        return result;
    }




}

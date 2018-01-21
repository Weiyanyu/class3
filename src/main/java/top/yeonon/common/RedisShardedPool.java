package top.yeonon.common;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import top.yeonon.pojo.User;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.UserInfoVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisShardedPool {
    private static ShardedJedisPool pool;
    private static Integer maxTotal = PropertiesUtil.getIntegerProperty("redis.max.total",20);
    private static Integer maxIdle = PropertiesUtil.getIntegerProperty("redis.max.idle",10);
    private static Integer minIdle = PropertiesUtil.getIntegerProperty("redis.min.idle",0);
    private static Boolean testOnBorrow = PropertiesUtil.getBooleanProperty("redis.testOnBorrow",true);
    private static Boolean testOnReturn = PropertiesUtil.getBooleanProperty("redis.testOnReturn", false);

    private static final String redis1Ip = PropertiesUtil.getStringProperty("redis1.ip");
    private static final Integer redis1Port = PropertiesUtil.getIntegerProperty("redis1.port",6379);

    private static final String redis2Ip = PropertiesUtil.getStringProperty("redis2.ip");
    private static final Integer redis2Port = PropertiesUtil.getIntegerProperty("redis2.port",6380);

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 2 * 1000);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 2 * 1000);

        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList();
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList, Hashing.MURMUR_HASH, ShardedJedis.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenResouce(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        String seesionId = "124563";
        String mainKey = "class3:"+seesionId;
        String expires = "class3:expires";
        String expirations = "class3:expirations";
        if (RedisShardedPoolUtil.exists(expires)) {
            RedisShardedPoolUtil.expire(mainKey, 2100);
            RedisShardedPoolUtil.expire(expires, 1800);
            RedisShardedPoolUtil.expire(expirations, 2100);
            List<String> jsonList = RedisShardedPoolUtil.hmget(mainKey, "sessionAttr");
            if (CollectionUtils.isNotEmpty(jsonList) && jsonList.size() == 1) {
                UserInfoVo userInfoVo = JsonUtil.stringToObject(jsonList.get(0), UserInfoVo.class);
                if (userInfoVo != null) {
                    System.out.println(userInfoVo.getUserName());
                }
            }

        }
//        else {
//            if (RedisShardedPoolUtil.exists(mainKey)) {
//                RedisShardedPoolUtil.expire(mainKey, 2100);
//                RedisShardedPoolUtil.append(expires, "");
//                RedisShardedPoolUtil.expire(expires, 1800);
//                RedisShardedPoolUtil.sadd(expirations, seesionId);
//                RedisShardedPoolUtil.expire(expirations, 2100);
//                System.out.println("已经过期，但是有冗余");
//            }
//            else {
//                Map<String, String> hash = new HashMap<>();
//                UserInfoVo userInfoVo = new UserInfoVo();
//                userInfoVo.setUserName("yeonon");
//                userInfoVo.setStudentId("2015010622");
//                hash.put("sessionAttr", JsonUtil.objToString(userInfoVo));
//                RedisShardedPoolUtil.hmset(mainKey, hash);
//                RedisShardedPoolUtil.expire(mainKey, 2100);
//                RedisShardedPoolUtil.append(expires, "");
//                RedisShardedPoolUtil.expire(expires, 1800);
//                RedisShardedPoolUtil.sadd(expirations, seesionId);
//                RedisShardedPoolUtil.expire(expirations, 2100);
//                System.out.println("过期，但是没有冗余，故重新创建");
//            }
//        }
    }
}

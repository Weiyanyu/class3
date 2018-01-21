package top.yeonon.util;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 可以作为一种cache实现的思路,在登陆中实现并不是很好，因为还要验证账号密码的正确性，除非后台service也改，
 * 否则并不能很好的解决问题。
 *
 * 大致讲一下吧：这个类的目的本来是有一个冗余的信息在redis里，这是因为有其他约束（这里是expires），所以即使
 * 信息还在redis里，用户也是无法获取的，这个时候如果在冗余信息到期之前，再次有请求，可以直接拿冗余信息，设置约束
 * 即可，并不需要从数据库里读取，这样就减少了磁盘IO，不过在登陆中并不好实现，因为需要验证账号密码，同时返回用户信息，
 * 这样一来，这里的冗余就失去了意义。
 */
public class RedisSessionUtil {
    private final static String SESSION_PRIFIX = "class3:";
    private final static String SESSION_ATTR = "user_info";

    private final static String expires = SESSION_PRIFIX + "expires";

    private final static String expirations = SESSION_PRIFIX + "expirations";

    public static void withCache(String sessionId) {
        if (sessionId == null) {
            return;
        }
        String mainKey = SESSION_PRIFIX + sessionId;

        RedisShardedPoolUtil.expire(mainKey, 2100);
        RedisShardedPoolUtil.append(expires, "");
        RedisShardedPoolUtil.expire(expires, 1800);
        RedisShardedPoolUtil.sadd(expirations, sessionId);
        RedisShardedPoolUtil.expire(expirations, 2100);
        System.out.println("已经过期，但是有冗余");
    }

    public static <T> void withoutCache(HttpServletRequest request, T userInfoVo) {
        String loginToken = CookieUtil.readCookie(request);
        if (loginToken == null) {
            return;
        }
        String mainKey = SESSION_PRIFIX + loginToken;

        Map<String, String> hash = new HashMap<>();

        hash.put(SESSION_ATTR, JsonUtil.objToString(userInfoVo));
        RedisShardedPoolUtil.hmset(mainKey, hash);
        RedisShardedPoolUtil.expire(mainKey, 2100);
        RedisShardedPoolUtil.append(expires, "");
        RedisShardedPoolUtil.expire(expires, 1800);
        RedisShardedPoolUtil.sadd(expirations, loginToken);
        RedisShardedPoolUtil.expire(expirations, 2100);
        System.out.println("过期，但是没有冗余，故重新创建");
    }

    public static <T> T getCache(HttpServletRequest request) {
        String loginToken = CookieUtil.readCookie(request);
        if (loginToken == null) {
            return null;
        }

        String mainKey = SESSION_PRIFIX + loginToken;

        RedisShardedPoolUtil.expire(mainKey, 2100);
        RedisShardedPoolUtil.expire(expires, 1800);
        RedisShardedPoolUtil.expire(expirations, 2100);
        List<String> jsonList = RedisShardedPoolUtil.hmget(mainKey, "sessionAttr");
        if (CollectionUtils.isNotEmpty(jsonList) && jsonList.size() == 1) {
            //因为类型未知，所以使用jackson里的TypeReference来转换成具体的对象
            T info = JsonUtil.stringToObject(jsonList.get(0), new TypeReference<T>() {
            });
            if (info != null) {
                return info;
            }
        }
        return null;
    }

    public static void delCache() {
        RedisShardedPoolUtil.del(expirations);
        RedisShardedPoolUtil.del(expires);
    }
}

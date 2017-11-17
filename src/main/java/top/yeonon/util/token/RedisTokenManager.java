package top.yeonon.util.token;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import top.yeonon.common.Const;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {


    private RedisTemplate<Integer, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<Integer, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    @Override
    public TokenModel createToken(Integer userId) {
        String token = UUID.randomUUID().toString().replace("_","");
        TokenModel model = new TokenModel(userId, token);
        redisTemplate.boundValueOps(userId).set(token, Const.LOGIN_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 2) {
            return null;
        }
        Integer userId = Integer.parseInt(param[0]);
        String token = param[1];
        return new TokenModel(userId, token);
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = redisTemplate.boundValueOps(model.getUserId()).get();
        if (token == null || !StringUtils.equals(token, model.getToken())) {
            return false;
        }

        //到这说明已经验证成功了，给当前用户延长token过期时间

        redisTemplate.boundValueOps(model.getUserId()).expire(Const.LOGIN_TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    @Override
    public void deleteToken(Integer userId) {
        //非法的删除
        if (userId == null) {
            return;
        }
        redisTemplate.delete(userId);
    }
}

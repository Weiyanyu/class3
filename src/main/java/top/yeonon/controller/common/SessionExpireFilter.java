package top.yeonon.controller.common;

import org.apache.commons.lang3.StringUtils;
import top.yeonon.common.Const;
import top.yeonon.util.CookieUtil;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionExpireFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String loginToken = CookieUtil.readCookie(httpServletRequest);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJson = RedisShardedPoolUtil.get(loginToken);
            UserInfoVo userInfoVo = JsonUtil.stringToObject(userJson, UserInfoVo.class);
            if (userInfoVo != null) {
                RedisShardedPoolUtil.expire(loginToken, Const.RedisCache.exTime);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

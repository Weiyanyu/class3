package top.yeonon.controller.front;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;
import top.yeonon.util.CookieUtil;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class LoginController {
    
    @Autowired
    private IUserService userService;

    /**
     *用户登录
     */
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<UserInfoVo> login(String studentId, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<UserInfoVo> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            CookieUtil.writeCookie(httpServletResponse, session.getId());
            RedisShardedPoolUtil.setex(session.getId(), JsonUtil.objToString(response.getData()),
                    Const.RedisCache.exTime);
        }
        return response;
    }

    /**
     *用户退出
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse<String> logout(HttpServletResponse response, HttpServletRequest request) {
        String loginToken = CookieUtil.readCookie(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("退出登陆失败，因为当前并没有登录");
        }
        CookieUtil.delCookie(request, response);
        Long result = RedisShardedPoolUtil.del(loginToken);
        if (result == null) {
            return ServerResponse.createByErrorMessage("退出失败，出现异常");
        }
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    /**
     *获取session存储的信息（主要用于检查是否登录）
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<UserInfoVo> getSession(HttpServletRequest request) {
        String loginToken = CookieUtil.readCookie(request);
        String userJson = RedisShardedPoolUtil.get(loginToken);
        UserInfoVo currentUser = JsonUtil.stringToObject(userJson, UserInfoVo.class);
        return ServerResponse.createBySuccess(currentUser);
    }

}

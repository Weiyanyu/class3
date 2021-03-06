package top.yeonon.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;
import top.yeonon.util.CookieUtil;
import top.yeonon.util.JsonUtil;
import top.yeonon.util.RedisShardedPoolUtil;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("manage/session")
public class LoginMangerController {

    @Autowired
    private IUserService userService;

    /**
     *后台管理员登录，要验证管理员身份
     */
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<UserInfoVo> login(String studentId, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<UserInfoVo> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            ServerResponse checkResponse = userService.checkRole(response.getData().getUserId());
            if (checkResponse.isSuccess()) {
                CookieUtil.writeCookie(httpServletResponse, session.getId());
                RedisShardedPoolUtil.setex(session.getId(), JsonUtil.objToString(response.getData()),
                        Const.RedisCache.exTime);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，请登录管理员账号");
            }
        }
        return response;
    }
}

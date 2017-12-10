package top.yeonon.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;
import top.yeonon.vo.UserInfoVo;

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
    public ServerResponse<UserInfoVo> login(String studentId, String password, HttpSession session) {
        ServerResponse<UserInfoVo> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     *用户退出
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }

    /**
     *获取session存储的信息（主要用于检查是否登录）
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<UserInfoVo> getSession(HttpSession session) {
        UserInfoVo currentUser = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(currentUser);
    }

}

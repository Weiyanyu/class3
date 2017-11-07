/**
 * 用户个人控制器，因为个人信息的操作比较敏感，所以将其从用户控制器中抽离出来，方便管理
 */

package top.yeonon.controller.front;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/personal")
public class PersonalController {
    @Autowired
    private IUserService userService;

    /**
     *
     * @param session           框架自动填入
     * @return
     *
     * 查看个人信息（比较多,完整，而且比较私密）
     */

    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<User> getPersonalInfo(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.CURRENT_USER);
        if (userId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }
        return userService.getPersonalInfo(userId);
    }


    /**
     *
     * @param studentId                 学号
     * @return
     * 忘记密码的情况下获取指定学号的密保问题
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public ServerResponse<String> forgetGetQuestion(String studentId) {
        return userService.getQuestion(studentId);
    }

    /**
     *
     * @param studentId                 学号
     * @param question                  密保问题
     * @param answer                    密保答案
     * @return
     * 用户输入答案，服务器来检查该密保答案是否和密保问题对应
     */
    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public ServerResponse forgetCheckAnswer(String studentId, String question, String answer) {
        return userService.checkAnswer(studentId, question, answer);
    }

    /***
     *
     * @param studentId                     学号
     * @param newPassword                   新密码
     * @param token                         token（服务器产生的）
     * @param session                       框架自动填入
     * @return
     * 用户输入密码正确后，前端调用这个接口，用户输入新密码，TOKEN验证正确后，即可修改密码，完成后返回一个status : 10，指明
     * 前端需要让用户重新登录
     */
    @RequestMapping(value = "/password/set", method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(String studentId, String newPassword, String token, HttpSession session) {
        ServerResponse<String> response = userService.forgetResetPassword(studentId, newPassword, token);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    /**
     *
     * @param oldPassword                   旧密码
     * @param newPassword                   新密码
     * @param session                       框架自动填入
     * @return
     * 这是在用户登录的情况下，直接使用原密码修改密码的接口,需要用户在登录状态下，有@CustomerPermission注解拦截
     */

    @CustomerPermission
    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, HttpSession session) {
        ServerResponse<String> response = userService.resetPassword((Integer) session.getAttribute(Const.CURRENT_USER), oldPassword, newPassword);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    /**
     *
     * @param user                          必填,（关键信息）
     * @param session                       框架自动注入
     * @return
     * 更新用户的个人信息，需要登录状态，有@CustomerPermission注解拦截控制
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse updateInfo(User user, HttpSession session) {
        Integer userId = (Integer) session.getAttribute(Const.CURRENT_USER);
        user.setUserId(userId);
        return userService.updateInfo(user);
    }
}

package top.yeonon.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.IUserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String studentId, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());

        }
        return response;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }


    @RequestMapping(value = "check_valid", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse checkValid(String str, String type) {

        return userService.checkValid(str, type);
    }

    @RequestMapping(value = "get_user_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "forget_get_question" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String studentId) {
        return userService.getQuestion(studentId);
    }


    @RequestMapping(value = "forget_check_answer", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetCheckAnswer(String studentId, String question, String answer) {
        return userService.checkAnswer(studentId, question, answer);
    }

    @RequestMapping(value = "forget_reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String studentId, String newPassword, String token) {
        return userService.forgetResetPassword(studentId, newPassword, token);
    }

    @RequestMapping(value = "reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        ServerResponse<String> response = userService.resetPassword(user, oldPassword, newPassword);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    @RequestMapping(value = "update_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInfo(User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }

        user.setUserId(currentUser.getUserId());
        user.setStudentId(currentUser.getStudentId());
        return userService.updateInfo(user);
    }

    @RequestMapping(value = "force_get_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forceGetInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录,强制登录status=10");
        }
        return userService.forceGetInfo(user.getUserId());
    }

    @RequestMapping(value = "logout" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }
}

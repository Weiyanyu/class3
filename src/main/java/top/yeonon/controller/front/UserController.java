package top.yeonon.controller.front;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.interceptor.PermissionInterceptor;
import top.yeonon.pojo.User;
import top.yeonon.service.IFileService;
import top.yeonon.service.IMailSenderService;
import top.yeonon.service.IUserService;
import top.yeonon.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IMailSenderService mailSenderService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String studentId, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(studentId, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());

        }
        return response;
    }

    @RequestMapping("upload_avatar")
    @ResponseBody
    public ServerResponse uploadAvatar(@RequestParam(value = "upload_file", required = false)MultipartFile upload_file,
                                       HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(upload_file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
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


    @CustomerPermission
    @RequestMapping(value = "get_user_info", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        return ServerResponse.createBySuccess((User) session.getAttribute(Const.CURRENT_USER));
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
    public ServerResponse<String> forgetResetPassword(String studentId, String newPassword, String token, HttpSession session) {
        ServerResponse<String> response = userService.forgetResetPassword(studentId, newPassword, token);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    @CustomerPermission
    @RequestMapping(value = "reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, HttpSession session) {
        ServerResponse<String> response = userService.resetPassword((User) session.getAttribute(Const.CURRENT_USER), oldPassword, newPassword);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    @CustomerPermission
    @RequestMapping(value = "update_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInfo(User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);

        user.setUserId(currentUser.getUserId());
        user.setStudentId(currentUser.getStudentId());
        return userService.updateInfo(user);
    }

    @RequestMapping(value = "force_get_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forceGetInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return userService.forceGetInfo(user.getUserId());
    }

    @RequestMapping(value = "logout" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("退出登录成功");
    }


    //以下是邮箱发送功能简单测试，暂时不作为正式代码
    @CustomerPermission
    @RequestMapping("send_mail")
    @ResponseBody
    public ServerResponse sendMail(String to, String content) {
        return mailSenderService.sendMail(to, content);
    }
}

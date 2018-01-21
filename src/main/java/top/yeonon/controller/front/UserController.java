/*
    用户控制器（前台），主要功能是获取各个用户可以公开的信息（评论数，发布过的消息等等）
 */

package top.yeonon.controller.front;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IFileService;
import top.yeonon.service.IMailSenderService;
import top.yeonon.service.IUserService;
import top.yeonon.util.PropertiesUtil;
import top.yeonon.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IMailSenderService mailSenderService;


    /**
     *
     * 注册一个用户
     */
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }


    /**
     *
     * 检查学号或者邮箱是否合法（主要是检查是否已经存在）
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ServerResponse checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    /**
     *
     * 获取用户信息，这里指的是可以公开的信息
     */
    //查看其它用户的信息,用户可以公开的信息,比如头像，个人简介等等。
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ServerResponse<UserInfoVo> getPublicInfo(@PathVariable("userId") Integer userId) {
        return userService.getPublicInfo(userId);
    }


    /**
     *
     * 忘记密码的情况下获取指定学号的密保问题
     */
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public ServerResponse<String> getQuestion(String studentId) {
        return userService.getQuestion(studentId);
    }

    /**
     *
     * 用户输入答案，服务器来检查该密保答案是否和密保问题对应
     */
    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public ServerResponse checkAnswer(String studentId, String question, String answer) {
        return userService.checkAnswer(studentId, question, answer);
    }





    /***
     *
     * 用户输入密码正确后，前端调用这个接口，用户输入新密码，TOKEN验证正确后，即可修改密码，完成后返回一个status : 10，指明
     * 前端需要让用户重新登录
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(String studentId, String newPassword, String token, HttpSession session) {
        ServerResponse<String> response = userService.forgetResetPassword(studentId, newPassword, token);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    /**
     *
     * 这是在用户登录的情况下，直接使用原密码修改密码的接口,需要用户在登录状态下，有@CustomerPermission注解拦截
     */

    @CustomerPermission
    @RequestMapping(value = "/password/update", method = RequestMethod.POST)
    public ServerResponse<String> updatePassword(String oldPassword, String newPassword, HttpSession session) {
        UserInfoVo currentUser = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        ServerResponse<String> response = userService.resetPassword(currentUser.getUserId(), oldPassword, newPassword);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    /**
     * 更新用户的个人信息，需要登录状态，有@CustomerPermission注解拦截控制
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse updateInfo(User user, HttpSession session) {
        UserInfoVo currentUser = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);
        user.setUserId(currentUser.getUserId());
        return userService.updateInfo(user);
    }


    /**
     *
     * 上传头像
     */
    @SuppressWarnings("unchecked")
    @CustomerPermission
    @RequestMapping(value = "avatar/upload", method = RequestMethod.POST)
    public ServerResponse uploadAvatar(@RequestParam(value = "avatar", required = false)MultipartFile avatar,
                                       HttpServletRequest request, HttpSession session) {
        UserInfoVo currentUser = (UserInfoVo) session.getAttribute(Const.CURRENT_USER);

        String path = request.getSession().getServletContext().getRealPath("upload");
        String remotePath = "img/avatar";
        String avatarName = currentUser.getUserId() + "-avatar";
        String targetFileName = fileService.upload(avatar, path, remotePath, avatarName);
        String url = PropertiesUtil.getStringProperty("ftp.server.http.prefix") + "avatar/" + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }


    /**
     *
     * 发送通知邮箱，比如找回密码的时候，连同token一起发送给用户
     */
    //以下是邮箱发送功能简单测试，暂时不作为正式代码
    @CustomerPermission
    @RequestMapping("send_mail")
    public ServerResponse sendMail(String to, String content) {
        return mailSenderService.sendMail(to, content);
    }
}

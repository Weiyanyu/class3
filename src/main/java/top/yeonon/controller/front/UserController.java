package top.yeonon.controller.front;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IFileService;
import top.yeonon.service.IMailSenderService;
import top.yeonon.service.IUserService;
import top.yeonon.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private IMailSenderService mailSenderService;


    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }


    @RequestMapping(value = "upload_avatar")
    public ServerResponse uploadAvatar(@RequestParam(value = "wangEditorH5File", required = false)MultipartFile wangEditorH5File,
                                       HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(wangEditorH5File, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }


    @RequestMapping(value = "/{type}/check", method = RequestMethod.GET)
    public ServerResponse checkValid(String str, @PathVariable("type") String type) {
        return userService.checkValid(str, type);
    }


    /*TODO 未来要拓展可以查看他人信息*/
    @CustomerPermission
    @RequestMapping(value = "/{studentId}", method = RequestMethod.GET)
    public ServerResponse<User> getUserInfo(@PathVariable("studentId") Integer studentId, HttpSession session) {
        return ServerResponse.createBySuccess((User) session.getAttribute(Const.CURRENT_USER));
    }

    /*TODO 这里要改动*/
    @RequestMapping(value = "{studentId}/question", method = RequestMethod.GET)
    public ServerResponse<String> forgetGetQuestion(@PathVariable("studentId") String studentId) {
        return userService.getQuestion(studentId);
    }


    @RequestMapping(value = "{studentId}/answer", method = RequestMethod.GET)
    public ServerResponse forgetCheckAnswer(@PathVariable("studentId") String studentId, String question, String answer) {
        return userService.checkAnswer(studentId, question, answer);
    }

    @RequestMapping(value = "{studentId}/password/set", method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(@PathVariable("studentId") String studentId, String newPassword, String token, HttpSession session) {
        ServerResponse<String> response = userService.forgetResetPassword(studentId, newPassword, token);
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    @CustomerPermission
    @RequestMapping(value = "{studentId}/password/update", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(@PathVariable("studentId") Integer studentId, String oldPassword, String newPassword, HttpSession session) {
        ServerResponse<String> response = userService.resetPassword((User) session.getAttribute(Const.CURRENT_USER), oldPassword, newPassword);
        if (response.isSuccess()) {

            session.removeAttribute(Const.CURRENT_USER);
        }
        return response;
    }

    @CustomerPermission
    @RequestMapping(value = "{studentId}", method = RequestMethod.PUT)
    public ServerResponse updateInfo(@PathVariable String studentId , User user, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        user.setUserId(currentUser.getUserId());
        user.setStudentId(currentUser.getStudentId());
        return userService.updateInfo(user);
    }

//    @RequestMapping(value = "force_get_info", method = RequestMethod.POST)
//    public ServerResponse forceGetInfo(HttpSession session) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，强制登录status=10");
//        }
//        return userService.forceGetInfo(user.getUserId());
//    }




    //以下是邮箱发送功能简单测试，暂时不作为正式代码
    @CustomerPermission
    @RequestMapping("send_mail")
    
    public ServerResponse sendMail(String to, String content) {
        return mailSenderService.sendMail(to, content);
    }
}

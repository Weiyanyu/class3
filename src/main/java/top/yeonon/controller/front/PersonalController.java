/**
 * 用户个人控制器，因为个人信息的操作比较敏感，所以将其从用户控制器中抽离出来，方便管理
 */

package top.yeonon.controller.front;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.Const;
import top.yeonon.common.ResponseCode;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IFileService;
import top.yeonon.service.IUserService;
import top.yeonon.util.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/personal")
public class PersonalController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IFileService fileService;

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
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }
        return userService.getPersonalInfo(user.getUserId());
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
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse<String> response = userService.resetPassword(currentUser.getUserId(), oldPassword, newPassword);
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
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        user.setUserId(currentUser.getUserId());
        return userService.updateInfo(user);
    }

    /**
     *
     * @param avatar            文件类型， 必填
     * @param request           框架自动填入
     * @return
     * 上传头像
     */
    @RequestMapping(value = "avatar/upload", method = RequestMethod.POST)
    public ServerResponse uploadAvatar(@RequestParam(value = "avatar", required = false)MultipartFile avatar,
                                       HttpServletRequest request, HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(avatar, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }
}

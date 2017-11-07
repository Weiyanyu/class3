/*
    用户控制器（前台），主要功能是获取各个用户可以公开的信息（评论数，发布过的消息等等）
 */

package top.yeonon.controller.front;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.IFileService;
import top.yeonon.service.IMailSenderService;
import top.yeonon.service.IUserService;
import top.yeonon.util.PropertiesUtil;
import javax.servlet.http.HttpServletRequest;
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
     * @param user          必填
     * @return
     *
     * 注册一个用户
     */
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }

    /**
     *
     * @param avatar            文件类型， 必填
     * @param request           框架自动填入
     * @return
     * 上传头像
     */
    @RequestMapping(value = "upload_avatar")
    public ServerResponse uploadAvatar(@RequestParam(value = "avatar", required = false)MultipartFile avatar,
                                       HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(avatar, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     *
     * @param str           学号或者邮箱字符串， 必填
     * @param type          学号或者邮箱,表示类型的参数, 必填
     * @return 检查学号或者邮箱是否合法（主要是检查是否已经存在）
     */
    @RequestMapping(value = "/{type}/check", method = RequestMethod.GET)
    public ServerResponse checkValid(String str, @PathVariable("type") String type) {
        return userService.checkValid(str, type);
    }

    /**
     *
     * @param studentId             某个用户的学号,必填
     * @return
     * 获取用户信息，这里指的是可以公开的信息
     */
    //查看其它用户的信息,用户可以公开的信息,比如头像，个人简介等等。
    @RequestMapping(value = "/{studentId}", method = RequestMethod.GET)
    public ServerResponse<User> getUserInfo(@PathVariable("studentId") String studentId) {
        return userService.getPublicInfo(studentId);
    }


    /**
     *
     * @param to                        目标邮箱
     * @param content                   邮件内容
     * @return
     * 发送通知邮箱，比如找回密码的时候，连同token一起发送给用户
     */
    //以下是邮箱发送功能简单测试，暂时不作为正式代码
    @CustomerPermission
    @RequestMapping("send_mail")
    public ServerResponse sendMail(String to, String content) {
        return mailSenderService.sendMail(to, content);
    }
}

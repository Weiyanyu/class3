package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;

import top.yeonon.pojo.Topic;
import top.yeonon.pojo.User;
import top.yeonon.service.ITopicService;
import top.yeonon.service.IUserService;


import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/topic")
public class TopicManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ITopicService topicService;

    @RequestMapping("add_topic.do")
    @ResponseBody
    public ServerResponse<String> addTopic(String topicName, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return topicService.addTopic(topicName);
    }


    @RequestMapping("batch_delete_topic.do")
    @ResponseBody
    public ServerResponse<String> batchDeleteTopic(String topicIds, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }

        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return topicService.batchDelete(topicIds);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }

        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return topicService.getTopicList(pageNum, pageSize);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String topicName,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }

        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return topicService.searchTopic(topicName, pageNum, pageSize);

    }


    @RequestMapping("update_topic.do")
    @ResponseBody
    public ServerResponse update(Topic topic, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }

        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }

        return topicService.updateTopic(topic);
    }
}

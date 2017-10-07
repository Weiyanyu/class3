package top.yeonon.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.service.IUserService;
import top.yeonon.service.Impl.NoticeService;
import top.yeonon.service.Impl.UserService;
import top.yeonon.vo.NoticeDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/notice/")
public class NoticeManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private INoticeService noticeService;

    @RequestMapping("add_notice")
    @ResponseBody
    public ServerResponse add(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        notice.setUserId(user.getUserId());
        return noticeService.addNotice(notice);
    }

    @RequestMapping("batch_delete_notice")
    @ResponseBody
    public ServerResponse batchDelete(String noticeIds, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }

        return noticeService.batchDeleteNotice(noticeIds);
    }


    @RequestMapping("list_notice")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
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
        return noticeService.getNoticeList(pageNum, pageSize, null);
    }



    @RequestMapping("list_notice_by_topic")
    @ResponseBody
    public ServerResponse listByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      Integer topicId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @RequestMapping("detail_notice")
    @ResponseBody
    public ServerResponse<NoticeDetailVo> detail(Integer noticeId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        return noticeService.getDetail(noticeId);
    }


    @RequestMapping("update_notice")
    @ResponseBody
    public ServerResponse update(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录管理员账号");
        }
        ServerResponse validResponse = userService.checkRole(user);
        if (!validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限，请登录管理员账号");
        }
        notice.setUserId(user.getUserId());
        return noticeService.updateNotice(notice);
    }
}

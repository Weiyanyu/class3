package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Notice;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.service.IUserService;
import top.yeonon.service.Impl.NoticeService;
import top.yeonon.service.Impl.UserService;
import top.yeonon.vo.NoticeDetailVo;
import top.yeonon.vo.NoticeListVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/notices/")
public class NoticeManageController {

    @Autowired
    private INoticeService noticeService;

    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.addNotice(notice);
    }

    @ManagerPermission
    @RequestMapping(value = "{noticeIds}", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse batchDelete(@PathVariable("noticeIds") String noticeIds) {

        return noticeService.batchDeleteNotice(noticeIds);
    }


    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      Integer topicId) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @ManagerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           String noticeTitle) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle);
    }

    @ManagerPermission
    @RequestMapping(value = "{noticeId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<NoticeDetailVo> detail(@PathVariable("noticeId") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }


    @ManagerPermission
    @RequestMapping(value = "{noticeId}", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse update(@PathVariable("noticeId") Integer noticeId,
                                 Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.updateNotice(noticeId,notice);
    }
}

package top.yeonon.controller.backend;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/manage/notice/")
public class NoticeManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private INoticeService noticeService;

    @ManagerPermission
    @RequestMapping("add_notice")
    @ResponseBody
    public ServerResponse add(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.addNotice(notice);
    }

    @ManagerPermission
    @RequestMapping("batch_delete_notice")
    @ResponseBody
    public ServerResponse batchDelete(String noticeIds, HttpSession session) {

        return noticeService.batchDeleteNotice(noticeIds);
    }

    @ManagerPermission
    @RequestMapping("list_notice")
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               HttpSession session) {
        return noticeService.getNoticeList(pageNum, pageSize, null);
    }



    @ManagerPermission
    @RequestMapping("list_notice_by_topic")
    @ResponseBody
    public ServerResponse listByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      Integer topicId, HttpSession session) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @ManagerPermission
    @RequestMapping("search_notice")
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           String noticeTitle, HttpSession session) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle);
    }

    @ManagerPermission
    @RequestMapping("detail_notice")
    @ResponseBody
    public ServerResponse<NoticeDetailVo> detail(Integer noticeId, HttpSession session) {
        return noticeService.getDetail(noticeId);
    }


    @ManagerPermission
    @RequestMapping("update_notice")
    @ResponseBody
    public ServerResponse update(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.updateNotice(notice);
    }
}

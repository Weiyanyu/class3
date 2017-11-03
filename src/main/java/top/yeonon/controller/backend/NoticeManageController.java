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
import top.yeonon.vo.NoticeDetailVo;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/notices/")
public class NoticeManageController {

    @Autowired
    private INoticeService noticeService;

    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse add(Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.addNotice(notice);
    }

    @ManagerPermission
    @RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
    public ServerResponse batchDelete(@PathVariable("ids") String noticeIds) {

        return noticeService.batchDeleteNotice(noticeIds);
    }


    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse listByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      Integer topicId, String orderBy) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId, orderBy);
    }
    @ManagerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                           String noticeTitle, String orderBy) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle, orderBy);
    }
    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<NoticeDetailVo> detail(@PathVariable("id") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }


    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ServerResponse update(@PathVariable("id") Integer noticeId,
                                 Notice notice, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        notice.setUserId(user.getUserId());
        return noticeService.updateNotice(noticeId,notice);
    }
}

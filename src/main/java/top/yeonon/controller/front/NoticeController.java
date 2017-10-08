package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.vo.NoticeDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice/")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @RequestMapping("all_list")
    @ResponseBody
    public ServerResponse<PageInfo> getAllList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return noticeService.getNoticeList(pageNum, pageSize, null);
    }

    @RequestMapping("list_by_topic")
    @ResponseBody
    public ServerResponse<PageInfo> getListByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                               Integer topicId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @RequestMapping("search_notice")
    @ResponseBody
    public ServerResponse<PageInfo> searchNotice(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   String noticeTitle, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle);
    }


    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<NoticeDetailVo> getListByTopic(Integer noticeId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录");
        }
        return noticeService.getDetail(noticeId);
    }

}

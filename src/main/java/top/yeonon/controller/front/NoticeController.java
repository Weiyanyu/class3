package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.vo.NoticeDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice/")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    @CustomerPermission
    @RequestMapping("all_list")
    @ResponseBody
    public ServerResponse<PageInfo> getAllList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            HttpSession session) {
        return noticeService.getNoticeList(pageNum, pageSize, null);
    }

    @CustomerPermission
    @RequestMapping("list_by_topic")
    @ResponseBody
    public ServerResponse<PageInfo> getListByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                               Integer topicId, HttpSession session) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @CustomerPermission
    @RequestMapping("search_notice")
    @ResponseBody
    public ServerResponse<PageInfo> searchNotice(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   String noticeTitle, HttpSession session) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle);
    }


    @CustomerPermission
    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<NoticeDetailVo> getListByTopic(Integer noticeId, HttpSession session) {
        return noticeService.getDetail(noticeId);
    }

}

package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.INoticeService;
import top.yeonon.vo.NoticeDetailVo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/notices/")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;



    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getListByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              Integer topicId) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId);
    }

    @CustomerPermission
    @RequestMapping(value = "search" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> searchNotice(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   String noticeTitle) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle);
    }


    @CustomerPermission
    @RequestMapping(value = "{noticeId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<NoticeDetailVo> getListByTopic(@PathVariable("noticeId") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }

}

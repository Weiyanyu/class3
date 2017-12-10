/**
 *  通知控制器(前台)
 *  功能主要是查
 */

package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.service.INoticeService;
import top.yeonon.vo.NoticeDetailVo;


@RestController
@RequestMapping("/notices/")
public class NoticeController {

    @Autowired
    private INoticeService noticeService;

    /**
     *获取notice 列表 (可以通过topic id获取， 默认是获取全部)
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> getListByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                    Integer topicId,
                                                    @RequestParam(value = "orderBy", defaultValue = "id_asc", required = false) String orderBy) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId, orderBy);
    }

    /**
     *查找notice（通过notice 的标题）
     */
    @CustomerPermission
    @RequestMapping(value = "search" , method = RequestMethod.GET)
    public ServerResponse<PageInfo> searchNotice(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   String noticeTitle, String orderBy) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle, orderBy);
    }


    /**
     *获取某个notice 的详细信息
     */
    @CustomerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<NoticeDetailVo> getDetail(@PathVariable("id") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ServerResponse getNoticesByUserId(@PathVariable("userId") Integer userId) {
        return noticeService.getNoticeListByUserId(userId);
    }


}

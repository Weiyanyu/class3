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
     *
     * @param pageNum       默认1， 可选
     * @param pageSize      默认10， 可选
     * @param topicId       主题ID, 可选，不输入即表示获取所有
     * @param orderBy       可选
     * @return
     * 获取所有通知或者根据主题id获取对应的通知（根据topicId参数是否为null）
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> getListByTopic(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              Integer topicId, String orderBy) {
        return noticeService.getNoticeList(pageNum, pageSize, topicId, orderBy);
    }

    /**
     *
     * @param pageNum       默认1， 可选
     * @param pageSize      默认10， 可选
     * @param noticeTitle   标题关键词
     * @param orderBy       可选
     * @return
     * 根据标题关键词搜索通知,功能比较弱
     */
    @CustomerPermission
    @RequestMapping(value = "search" , method = RequestMethod.GET)
    public ServerResponse<PageInfo> searchNotice(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                   String noticeTitle, String orderBy) {
        return noticeService.searchNotice(pageNum, pageSize, noticeTitle, orderBy);
    }


    /**
     *
     * @param noticeId  路径中的参数，必填
     * @return
     * 获取对应id的通知的详细内容
     */
    @CustomerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<NoticeDetailVo> getDetail(@PathVariable("id") Integer noticeId) {
        return noticeService.getDetail(noticeId);
    }

}

/**
 *  主题控制器(前台)
 *  前台的Topic仅仅允许查看，不允许增加，删除，更改
 */
package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.service.Impl.TopicService;




@RestController
@RequestMapping("/topics/")
public class TopicController {

    @Autowired
    private TopicService topicService;


    /***
     *
     * 获取所有主题
     */
    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            String orderBy) {
        return topicService.searchOrListAllTopic(null, pageNum, pageSize, orderBy);
    }


    /**
     *
     * 根据名称关键词搜索主题，暂时没有使用搜索引擎，能力比较弱
     */
    @CustomerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             String topicName, String orderBy) {
        return topicService.searchOrListAllTopic(topicName, pageNum, pageSize, orderBy);
    }


}

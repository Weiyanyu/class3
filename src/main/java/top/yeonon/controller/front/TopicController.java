package top.yeonon.controller.front;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.CustomerPermission;
import top.yeonon.pojo.User;
import top.yeonon.service.Impl.TopicService;
import top.yeonon.vo.TopicDetailVo;

import javax.servlet.http.HttpSession;


//前台的Topic仅仅允许查看，不允许增加，删除，更改
@RestController
@RequestMapping("/topics/")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @CustomerPermission
    @RequestMapping(method = RequestMethod.GET)
    
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            String orderBy) {
        return topicService.searchOrListAllTopic(null, pageNum, pageSize, orderBy);
    }

    @CustomerPermission
    @RequestMapping(value = "{topicId}", method = RequestMethod.GET)
    
    public ServerResponse<TopicDetailVo> getDetail(@PathVariable("topicId") Integer topicId) {
        return topicService.getTopicDetail(topicId);
    }

    @CustomerPermission
    @RequestMapping(value = "search", method = RequestMethod.GET)
    
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             String topicName, String orderBy) {
        return topicService.searchOrListAllTopic(topicName, pageNum, pageSize, orderBy);
    }


}

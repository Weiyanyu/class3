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
import top.yeonon.service.Impl.TopicService;
import top.yeonon.vo.TopicDetailVo;

import javax.servlet.http.HttpSession;


//前台的Topic仅仅允许查看，不允许增加，删除，更改
@Controller
@RequestMapping("/topic/")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @CustomerPermission
    @RequestMapping("list")
    @ResponseBody
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            String orderBy) {
        return topicService.searchOrListAllTopic(null, pageNum, pageSize, orderBy);
    }

    @CustomerPermission
    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<TopicDetailVo> getDetail(Integer topicId) {
        return topicService.getTopicDetail(topicId);
    }

    @CustomerPermission
    @RequestMapping("search")
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            String topicName, String orderBy) {
        return topicService.searchOrListAllTopic(topicName, pageNum, pageSize, orderBy);
    }


}

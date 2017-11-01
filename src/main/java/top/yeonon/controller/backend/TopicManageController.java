package top.yeonon.controller.backend;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.Const;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Topic;
import top.yeonon.pojo.User;
import top.yeonon.service.ITopicService;
import top.yeonon.service.IUserService;
import top.yeonon.vo.TopicDetailVo;


import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/topics/")
public class TopicManageController {


    @Autowired
    private ITopicService topicService;

    //添加主题，用过提交表单添加
    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addTopic(Topic topic) {
        return topicService.addTopic(topic);
    }

    //批量删除主题，单个删除同样可以使用
    @ManagerPermission
    @RequestMapping(value = "{topicIds}", method = RequestMethod.DELETE)
    @ResponseBody
    public ServerResponse<String> batchDeleteTopic(@PathVariable("topicIds") String topicIds) {
        return topicService.batchDelete(topicIds);
    }


    //返回List
    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String orderBy) {
        return topicService.searchOrListAllTopic(null, pageNum, pageSize, orderBy);
    }

    //模糊查询
    @ManagerPermission
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String topicName, String orderBy) {
        return topicService.searchOrListAllTopic(topicName, pageNum, pageSize, orderBy);

    }

    @ManagerPermission
    @RequestMapping(value = "{topicId}", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse update(@PathVariable("topicId") Integer topicId,
                                 Topic topic) {

        return topicService.updateTopic(topicId, topic);
    }

    @ManagerPermission
    @RequestMapping(value = "{topicId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<TopicDetailVo> detail(@PathVariable("topicId") Integer topicId) {
        return topicService.getTopicDetail(topicId);
    }
}

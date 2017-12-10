package top.yeonon.controller.backend;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.common.ServerResponse;
import top.yeonon.interceptor.ManagerPermission;
import top.yeonon.pojo.Topic;
import top.yeonon.service.ITopicService;




@RestController
@RequestMapping("/manage/topics/")
public class TopicManageController {


    @Autowired
    private ITopicService topicService;

    /**
     * 添加主题，通过提交表单添加
     */

    @ManagerPermission
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<String> addTopic(Topic topic) {
        return topicService.addTopic(topic);
    }

    /**
     * 批量删除主题，单个删除同样可以使用
     */
    @ManagerPermission
    @RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
    public ServerResponse<String> batchDeleteTopic(@PathVariable("ids") String topicIds) {
        return topicService.batchDelete(topicIds);
    }


    /**
     *   返回List
     */
    @ManagerPermission
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String orderBy) {
        return topicService.searchOrListAllTopic(null, pageNum, pageSize, orderBy);
    }

    /**
     * 模糊查询,通过主题的名称（暂时没有提供主题的描述，见名知义即可）
     */

    @ManagerPermission
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ServerResponse<PageInfo> search(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         String topicName, String orderBy) {
        return topicService.searchOrListAllTopic(topicName, pageNum, pageSize, orderBy);
    }

    /**
     *更新某个主题的信息
     */
    @ManagerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ServerResponse update(@PathVariable("id") Integer topicId,
                                 Topic topic) {
        return topicService.updateTopic(topicId, topic);
    }

}

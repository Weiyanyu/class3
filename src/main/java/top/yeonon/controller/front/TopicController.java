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
import top.yeonon.vo.TopicDetailVo;




@RestController
@RequestMapping("/topics/")
public class TopicController {

    @Autowired
    private TopicService topicService;


    /***
     *
     * @param pageNum   默认为1， 可选
     * @param pageSize  默认为10， 可选
     * @param orderBy   没有默认值， 可以为null, 可选
     * @return
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
     * @param topicId  需要传入参数ID,restful接口i，d可以再路径上
     * @return
     * 根据主题id来获取详细内容
     */
    @CustomerPermission
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ServerResponse<TopicDetailVo> getDetail(@PathVariable("id") Integer topicId) {
        return topicService.getTopicDetail(topicId);
    }

    /**
     *
     * @param pageNum       默认1， 可选
     * @param pageSize      默认10， 可选
     * @param topicName     理论上可选，但是最好不要为null，要不然没有意义，前端应该控制这里不能为空
     * @param orderBy       可选，无默认值
     * @return
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

package top.yeonon.service;
import com.github.pagehelper.PageInfo;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Topic;

public interface ITopicService {

    ServerResponse<String> addTopic(Topic topic);

    ServerResponse<String> batchDelete(String topicIds);

    ServerResponse<PageInfo> searchOrListAllTopic(String topicName, int pageNum, int pageSize, String orderBy);

    ServerResponse updateTopic(Integer topicId, Topic topic);
}

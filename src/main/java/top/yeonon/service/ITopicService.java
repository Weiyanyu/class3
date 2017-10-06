package top.yeonon.service;

import com.github.pagehelper.PageInfo;
import top.yeonon.common.ServerResponse;
import top.yeonon.pojo.Topic;

import java.util.List;

public interface ITopicService {

    ServerResponse<String> addTopic(String topicName);

    ServerResponse<String> batchDelete(String topicIds);

    ServerResponse<PageInfo> getTopicList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchTopic(String topicName, int pageNum, int pageSize);

    ServerResponse updateTopic(Topic topic);
}

package top.yeonon.service;

import top.yeonon.common.ServerResponse;

import java.util.List;

public interface ITopicService {

    ServerResponse<String> addTopic(String topicName);

    ServerResponse<String> batchDelete(String topicIds);
}
